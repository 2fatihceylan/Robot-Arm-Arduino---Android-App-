#include <SoftwareSerial.h>

#include <Servo.h>
Servo servodonme;
Servo servoilerigeri;
Servo servoyukariasagi;
Servo servotutucu;
String veri;

bool tut=false;

void setup() {
  // put your setup code here, to run once:
  servodonme.attach(5);    //dönme
  servoilerigeri.attach(6);    //ileri geri
  servoyukariasagi.attach(7);    //yukarı-aşağı
  servotutucu.attach(8);    //tutucu

  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
   while(Serial.available()) { //Seri haberleşmeden veri gelmesini bekliyoruz.
    delay(1);
    char c=Serial.read();
    veri+=c;
  }
 
  //donme(veri);
  Serial.println(veri);

  if(veri=="tut"){
    if(tut==false){
      tutucu(60);
      tut=true;
    }
    else{
      tutucu(0);
      tut=false;
    }
  }
  else if(veri=="sifirla"){
    sifirla();
  }
  else if(veri=="yap1"){
    yap();
  }
  else if(veri=="yap2"){
    yap2();
  }

  String s="";
  int str_len=veri.length()+1;
  char char_array[str_len];
  veri.toCharArray(char_array,str_len);
  
  if(char_array[0]=='X'){
    for (int i = 1; i < str_len; i++){
      s+=char_array[i];
    }
    int deger=s.toInt();
    donme(deger);
  }


  if(char_array[0]=='Y'){
    for (int i = 1; i < str_len; i++){
      s+=char_array[i];
    }
    int deger=s.toInt();
    
    ilerigeri(deger);
    
  }

  if(char_array[0]=='C'){
    for (int i = 1; i < str_len; i++){
      s+=char_array[i];
    }
    int deger=s.toInt();
    
     yukariasagi(deger);
    
  }
  

  veri="";
  s="";
}


void donme(int x){
  //0 180 arası
  servodonme.write(x);
  
}
void tutucu(int x){
  //0 ile 60 derece arası
  servotutucu.write(x);
  
}
void ilerigeri(int x){
  //180 derece en ileri
  //110 derece en geri
  servoilerigeri.write(x);
  
}
void yukariasagi(int x){
  //180 derece en yukarı
  //100 derece en asagı
  servoyukariasagi.write(x);
  
}
void sifirla(){
  servoilerigeri.write(110);
  servoyukariasagi.write(100);
  servotutucu.write(0);
  servodonme.write(90);
}





void yap2(){
   sifirla();
  delay(1000);
  yukariasagi(180);
  delay(800);
  ilerigeri(120);
  delay(800);
  tutucu(60);
  delay(500);
  tutucu(0);
  donme(45);
  delay(1000);
  donme(135);
  delay(2000);
}

void yap(){

  sifirla();
  delay(5000);

  ilerigeri(180);
  delay(2000);

  yukariasagi(180);
  delay(2000);

  tutucu(0);
  delay(1000);
  tutucu(60);
  delay(1000);
  tutucu(0);
  delay(1000);

  donme(0);
  delay(3000);
  donme(180);
  delay(3000);
}
