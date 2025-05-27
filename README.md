
# 🔥핫챗(채팅방에 초당 메시지 5개 이상) 배치 처리 시스템

## 🎯 목표

> **대규모 채팅 서비스에서 짧은 시간 동안 폭발적으로 쏟아지는 메시지를 효율적으로 처리하기 위해,**
> 
> 일정 기준 이상의 트래픽이 감지된 채팅방에 대해서는  
> **실시간 처리**에서 **배치 처리 방식**으로 전환하여  
> 시스템 부하를 줄이고, 서비스 안정성을 향상시키는 것을 목표로 합니다.

![스크린샷 2025-05-22 162109](https://github.com/user-attachments/assets/90e27f1f-c7f3-435e-b398-60788f73f2b4)

### 채팅 메시지가 들어올 때마다 Redis의 INCR 연산으로 1초 단위 메시지 수를 추적합니다. <br> 1초 동안 해당 채팅방의 메시지 수가 5건 이상일 경우, 해당 채팅방을 Hot Chat Room으로 간주합니다. <br> Redis의 `TTL(Time To Live)`을 **1초로 설정**하여 메시지 카운트가 자동으로 **초 단위로 만료**되도록 구성하였습니다.

<br>
<br>


![image](https://github.com/user-attachments/assets/e90a18b6-84fd-4bd5-ba39-104c8888e876)
### Map<Long, List<String>> 구조의 hotChatMessageBuffer에 메시지를 누적 저장하고 ScheduledExecutorService를 이용해 메시지를 1초 후 일괄 브로드캐스트하는 지연 배치 처리 구현 <br> 메시지 전송은 flushHotChatMessages(chatRoomId) 에서 한 번에 처리

<br>

![image](https://github.com/user-attachments/assets/094166b3-fe0e-4bbf-bbb4-6c7e96890e37)
### 해당 채팅방 메시지를 remove()하여 버퍼에서 제거하여 동시에 여러 flush가 발생하지 않도록 remove() 기반으로 중복 방지
