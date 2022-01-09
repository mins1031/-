# nonstop-deploy-practice
# 무중단 배포용 예제 프로젝트
## nginx + jenkins + spring boot 진행 예정
- 각각 예제용 프로젝트 서버, 젠킨스 서버를 두고 젠킨스 빌드시 해당 깃허브 주소의 프로젝트가 빌드되고 실행되게 진행했습니다.(AWS이용)
- 이제 무중단 배포진행
## 무중단 배포란?
! jojoldo 님의 무중단 배포 글을 참고하여 작성, 공부
> 예전엔 배포라고 하면 아주 큰 이벤트라 코드 합치는 날을 정해 합치고 배포 했다고 합니다. 보통 이용빈도가 적은 새벽에 배포를 진행했는데 만약 배포후에 서비스에 장애가 생기면...? 멘붕과 함께 부랴부랴 해결해야하고 만약 아침까지 고치치 못하면 긴급점검 올리고 해결하는...상황들이 많았다고 합니다. 이렇게 배포때마다 고생할 수 없기에 '서비스를 정지하지 않고 배포하는방법'을 찾기 시작했는데 그것이 바로 '무중단 배포' 입니다.
- 무중단 배포는 크게 3가지의 방법이 있지만 제일 싼.. 방법은 Nginx를 통한 무중단 배포 입니다.
- 구조는
  - 하나의 AWS 서버에 Nginx1대와 스프링부트 jar를 2개사용하는 것입니다.
  - Nginx는 80(http)와 443(https)포트를 사용하고
  - 스프링부트 jar1은 8081, jar2는 8082포트로 실행합니다.
- 운영과정은
  1) 사용자는 서비스 주소로 접속합니다(참고로 80은 http의 디폴트 포트, 443은 https의 디폴트 포트입니다)
  2) Nginx는 연결된 스프링부트로 요청을 전달합니다 -> 스프링부트1(8081)로 요청을 전달합니다
  3) 스프링부트2는 Nginx와 연결된 상태가 아니게 두어 요청 전달이 안됩니다.
  4) 신규 배포가 필요한 경우 Nginx와 연결되지 않은 스프링부트2로 배포가 됩니다  
  5) 배포동안에도 서비스는 중단되지 않습니다 -> 스프링부트1에 연결되어있기 때문입니다.
  6) 배포후 스프링부트2가 정상적으로 구동중인지 확인합니다
  7) 정상적이라면 nginx reload를 통해 8081에서 8082를 바라보도록 설정합니다.
  8) nginx reload는 1초 이내에 실행 완료됩니다.
  9) 또 새 버전을 배포해야 하는 상황에선 4~8의 과정이되 스프링부트2 -> 1로 변경됩니다.
  10) 만약 배포된 버전에서 문제가 생겼다면 바로 설정을 통해 기존의 배포 버전을 바라보게 하면 됩니다.
- 롤백도 간단하고 서비스도 중단없이 배포 할 수 있어 여러모로 익혀두면 유용하다 판단되어 공부하게 되었습니다.
- 이제 진행후에 돌아와 과정 서술 할 예정입니다.

- nginx 포트포워딩에 조금 삽질했다. 분명 con.f파일에 기본으로 8080포트를 바라보라고 하는 설정을 적어놨는데 계속 8080포트가 아닌 nginx메인 페이지(80)로 리다이렉트 되서.... 혹시나 해서 http{}내의 다른 설정들을 지워봤는데 성공했다.
- `include /etc/nginx/sites-enabled/*;` 해당 구문이 문제였다...
- 젠킨스로 빌드후 조치로 밑의 쉘 스크립트를 통해 놀고 있는 스프링부트 어플리케이션에 배포후 nginx가 배포된 스프링부트를 바라보게한다.
```
#!/bin/bash
echo "> 현재 구동중인 Port 확인"
CURRENT_PROFILE=$(curl -s http://localhost/profile)

# 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고, 반대면 set1이 쉬고 있>음
if [ $CURRENT_PROFILE == set1 ]
then
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE == set2 ]
then
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> 8081을 할당합니다."
  IDLE_PORT=8081
fi

echo "> 전환할 Port: $IDLE_PORT"
echo "> Port 전환"
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" |sudo tee /etc/nginx/conf.d/service-url.inc

PROXY_PORT=$(curl -s http://localhost/profile)
"switch.sh" 26L, 761C                                         1,1           Top
#!/bin/bash
echo "> 현재 구동중인 Port 확인"
CURRENT_PROFILE=$(curl -s http://localhost/profile)

# 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고, 반대면 set1이 쉬고 있>음
if [ $CURRENT_PROFILE == set1 ]
then
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE == set2 ]
then
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> 8081을 할당합니다."
  IDLE_PORT=8081
fi

echo "> 전환할 Port: $IDLE_PORT"
echo "> Port 전환"
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" |sudo tee /etc/nginx/conf.d/service-url.inc

PROXY_PORT=$(curl -s http://localhost/profile)
```
