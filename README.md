# ChatProgram

### 소켓 프로그래밍을 이용한 클라이언트-서버 통신으로 여러 사용자가 같이 채팅할 수 있는 채팅 프로그램입니다.


<br>
<br>

__프로그램에서 사용하는 프로토콜 목록__

![image](https://user-images.githubusercontent.com/57720521/193584898-8cb97e88-1302-4f29-b1e4-92f68ab27cd0.png)

__사용자는 보유 중인 이미지로 프로필 사진을 변경할 수 있습니다__
![image](https://user-images.githubusercontent.com/57720521/193585019-7e668e10-330e-4463-908a-c7976dd0dc3a.png)

__채팅창 사용 이미지입니다__

사용자는 이미지를 전송할 수 있고 채팅 프로그램에서 제공하는 이모티콘을 전송할 수 있습니다. <br>
이미지는 클릭하면 원본 크기로 볼 수 있습니다.


![image](https://user-images.githubusercontent.com/57720521/193585184-9b99cf9d-1a14-4e23-9213-91225eb44c91.png)

<br>
<br>
해당 프로그램은 2인 프로젝트로 진행했습니다.
저는 서버와 클라이언트 공통적인 ChatMember, ChatMsg를 작성했습니다.<br>
HanoTalkServer와 Room에서는 전반적인 채팅방 운영과 전송되는 스트림을 관리했습니다.<br>
HanoTalkClientLogin과 HanoTalkClientMain에서는 사용자의 프로필, 오프라인 유무 관리와 채팅방 생성, 관리했습니다.
<br><br>
개발하면서 가장 어려웠던 점은 각 사용자가 생성한 Room의 정보를 클라이언트와 서버가 온전히 같은 정보를 갖고 있어야한다는 점이었고,<br>
이를 위해 채팅방의 정보를 Vector로 전달하는 과정에서 오류가 발생했습니다.<br>
오류를 해결하기 위해서 Vector를 List로 바꾸어 보는 등의 시도를 했고,
해당 오류는 Room Class를 만들어서 관리하는 것으로 해결했습니다.<br>
Vector가 아닌 Class로 관리했을 때, 채팅방 정보를 저장하기도 수월했으며 채팅방에 속한 사용자들의 리스트를 관리할 수 있는 장점이 있었습니다.<br>
