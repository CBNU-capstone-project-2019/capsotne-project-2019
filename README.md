
# capstone_android_2020
"""
Created on Wed Aug  5 15:14:21 2020

@author: DaunJO
"""


## 2020.07.22

버튼 2개
- realTimeDetectionOptionButton => FaceDetectionActivity
imageDetectionOptionButton => ImageDetectionActivity

 따라서 FaceDetectionActivity로 

- FaceDetectionActiity
필요한 Camera Resources
CameraSource, CameraSourcePreview, GraphicOverlay

- EmotionDetection
create a custom SurfaceView that resizes itself to match the aspect ratio of the incoming camera frames.


- put EmotionDetect Code on ChatActivity
ChatActivity를 열고 Emotion Detect Start 버튼 누르면 -> Face Confirm -> Face Emotion Detect

- 앞으로 해야할것
1.  Emotion ratio말고 Highest Emotion 하나만 출력하기. -> 
2.  BackGround에서 EmotionDetect하도록 변경 (지금은 foreground에서 작동)
3.  BackGround에서 계속 감정 Detect하면서 가장 높은 ratio가진 Emotion을 ChatActivity에 출력하기



## 2020.07.29

점검
오늘은 Emotion액티비티와 Chat 액티비티를 합쳤다
- 그래서 Chat창에서 채팅 분석 버튼 클릭시 
-> python 서버 소켓 연결하여 채팅 데이터를 전송함
- 채팅 하면서 작은 카메라 Preview 생성함
-> 실시간 감정 분석하여 이모티콘으로 출력함
근데 linearlayout에서 Relative레이아웃으로 바꿔서
지금 레이아웃이 개판됨
- 어떻게해야될까?.....
** 릴레이티브 레리아웃에 대해 좀더공부하자
=> 카메라 프리뷰 위치랑 이모티콘 위치 아래로 내리자
=> 지금은 python 서버로 데이터 한번만 보내짐
=> 버튼 누를때마다 그때까지한 대화를 계속 보낼수 있또록?바꾸자
=> 내 감정 말고 대화 참여자들의 감정을 어떻게 가져 올것인가
=> python 서버에서 쏴주거나 or 채팅 msg보내는 firebase이용하기
- 일단은 firebase이용하는게 나을것같음..
firebase로 보내서. 채팅과 함께 수신하기
그리고 채팅이랑 감정 태그를 분리하여
감정 태그는 채팅창옆에(?) 이미지 뷰로 출력하면 좋을 것 같다



## 20200730

오늘한것

- 사진전송오류 => 사진전송시 튕김 : WIFI문제였나? 
firestore 연결이 오류났었음 -> 폴더 새로 연결하고 디버깅 계속 하니까 해결함

-  키보드 shown 시 툴바가 고정이 안됨 -> 포기함 카카오톡처럼 상단 타이틀은 고정하면서 밑에는 스크롤뷰?처럼 하고싶었는데
현재 내가 recycle뷰를 사용하면서 relative 뷰를 같이 연동해서? 그런지 아무리 해도 안됨
일단 포기. 그냥 툴바 가려지고 스크롤 뷰가 내려가면서 키보드 shown하도록 마무리함

- python 서버로 보내는 msg 형식 개선

- firestore에서 rooms에 있는 message의 UID랑 users콜렉션에 있는 usernm이랑 연결해서  유저네임(usernm)을 서버 보내는 msg에 같이 붙여서 보내고싶었는데
잘 안된다

- firestore 콜렉션을 두번 열어서 해봤는데
message의 UID와 users의 usernm을 매칭까지해서 log로 출력성공했으나
delay때문에 잘 안맞는다.
msg send후 usernm을 찾아와서 어떤애는 usernm 제대로 보내지고 어떤애는 usernm 안보내짐
그래서 걍 지움
아마도 내가 onComplete함수를 이용해서 그런것같은데
이부분은 어떻게 해야할지 고민중




## 20200804

오늘 한 것
- StopAnalysis 버튼 클릭시 감정 인식 중단 가능 => StopAnalsis를 result에 set하고 카메라 중단
- 감정 출력 smooth 출력되도록 result를 정확하게 연결하여 출력
- UI 개선
- 서버로 전달시에는 msg 형식 그대로 전달되지만
사용자가 볼때에는 msg를 split해서 좀 더 가독성있게 감정과 메세지를 구분해서 볼 수 있도록 #(해시태그)로 구분함
- 사용자 얼굴 인식 불가 시 Detecting... 출력하여 누적된 이전 감정이 출력되었던 오류 개선

느낀점
- 감정인식률이 낮긴한것같다(sdk사용해서어쩔수없나?)
사용자의 카메라 화면을 조금 더 키우면 인식률증가할수도있으나
그러면 UI화면이 가독성떨어짐
지금이 괜찮은듯

앞으로 해야할것
- server와 python모듈연동하여 msg분석
- msg 분석한 결과를 웹 파싱
- 웹 페이지를 안드로이드 분석 액티비티에 연결하기!
