#define BLYNK_TEMPLATE_ID "TMPL6EbX11fkb"
#define BLYNK_TEMPLATE_NAME "thungractm"
#define BLYNK_AUTH_TOKEN "SPjSUuyqRAa-Ysmc0C7DbPkqihlLvtuQ"

#include <BlynkSimpleEsp8266.h>
#include <Servo.h>

// Thông tin WiFi và Blynk
char auth[] = "SPjSUuyqRAa-Ysmc0C7DbPkqihlLvtuQ"; // Mã Auth từ Blynk
char ssid[] = "GwaHwang";  // Tên WiFi
char pass[] = "12345678";  // Mật khẩu WiFi

// Định nghĩa các chân kết nối
#define TRIG_PIN 5    // GPIO 5 (D1)
#define ECHO_PIN 4    // GPIO 4 (D2)
#define SERVO_PIN 14  // GPIO 14 (D5)

// Khởi tạo đối tượng Servo
Servo servoMotor;

long duration, distance;
unsigned long previousMillis = 0;
unsigned long autoMillis = 0;
unsigned char autoTrigger = 0;

// **Điều khiển nút V1**
BLYNK_WRITE(V1) {
  int buttonState = param.asInt(); // Đọc trạng thái nút từ Blynk
  if (buttonState == 1) {
    servoMotor.write(90); // Mở nắp (90 độ)
    Blynk.virtualWrite(V2, 255); // Bật LED trên Blynk
    Blynk.virtualWrite(V3, "Lid Opened"); // Cập nhật trạng thái
  } else {
    servoMotor.write(0); // Đóng nắp (0 độ)
    Blynk.virtualWrite(V2, 0); // Tắt LED trên Blynk
    Blynk.virtualWrite(V3, "Lid Closed"); // Cập nhật trạng thái
  }
}

void setup() {
  Serial.begin(115200);           // Khởi tạo giao tiếp Serial để debug
  Blynk.begin(auth, ssid, pass);  // Khởi động kết nối Blynk

  pinMode(TRIG_PIN, OUTPUT);      // Đặt chân Trig là OUTPUT
  pinMode(ECHO_PIN, INPUT);       // Đặt chân Echo là INPUT
  servoMotor.attach(SERVO_PIN);   // Gắn servo vào chân GPIO 14 (D5)
  servoMotor.write(0);            // Vị trí ban đầu của servo là đóng (0 độ)
}

void loop() {
  Blynk.run();  // Đảm bảo Blynk hoạt động

  // Đọc khoảng cách từ cảm biến siêu âm mỗi 100ms
  if (millis() - previousMillis >= 100) {
    previousMillis = millis();

    // Kích hoạt cảm biến siêu âm
    digitalWrite(TRIG_PIN, LOW);
    delayMicroseconds(2);
    digitalWrite(TRIG_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(TRIG_PIN, LOW);

    // Đọc chân Echo và tính toán khoảng cách
    duration = pulseIn(ECHO_PIN, HIGH);
    distance = duration * 0.034 / 2;  // Tính khoảng cách (cm)

    if (duration == 0) {
      Serial.println("No echo detected! Check connections and power.");
      distance = 0;
    }

    Serial.print("Distance: ");
    Serial.print(distance);
    Serial.println(" cm");

    // Gửi giá trị khoảng cách tới Blynk (Virtual Pin V0)
    Blynk.virtualWrite(V0, distance);

    // Tự động mở nắp khi khoảng cách < 15 cm
    if (distance < 15) {
      autoTrigger = 1;
      autoMillis = millis();
      servoMotor.write(90);  // Mở nắp (90 độ)
      Blynk.virtualWrite(V2, 255); // Bật LED trên Blynk
      Blynk.virtualWrite(V3, "Lid Opened");
    }
  }

  // Tự động đóng nắp sau 2 giây nếu đã mở
  if (millis() - autoMillis >= 2000 && autoTrigger == 1) {
    autoTrigger = 0;
    servoMotor.write(0);  // Đóng nắp (0 độ)
    Blynk.virtualWrite(V2, 0); // Tắt LED trên Blynk
    Blynk.virtualWrite(V3, "Lid Closed");
  }
}
