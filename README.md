# JWT기반 TODO 백엔드 API구현

---
<br>

## 🧑‍💻 실행 방법

### 전제 조건
- java 17.0.15
- Maven
- SQLite (기본 내장 DB 파일로 동작)

### 빌드 & 실행 (Maven 기준)
- application.properties에서 SQLite 파일 경로 및 JWT 시크릿을 설정합니다.

### 설명
> 로그인: POST /users/login → username/password 검증 → JwtTokenProvider.createToken() 호출 → JWT 발급

> 인증 필터: 보호된 엔드포인트 호출 시 JwtAuthenticationFilter 작동 → resolveToken() 및 validateToken() → getAuthentication()으로 Authentication 생성 → SecurityContextHolder에 설정

> 요청 처리: 인증이 완료된 상태로 각 컨트롤러 로직 실행

> 예외 처리: 토큰 누락/만료/위변조 시 401 Unauthorized 반환

> SecurityConfig 에서 /users/signup, /users/login 은 permitAll(), 나머지 모든 요청은 JWT 인증이 필요합니다.


---
<br>


## 📖 API 명세
- 각 API는 JSON 형태의 요청과 응답을 주고받으며, Content-Type : application/json 헤더를 설정해야 합니다.

### User API


<details markdown="1">
<summary>회원가입 `POST /users/signup`</summary>
 
#### Ruquest Body : 
```json
{
  "memberId": "string",
  "memberName": "string",
  "memberPw": "string"
}
```
 
<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
"status": 201,
  "message": "회원가입 성공"
}
```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json

{
"status": 400,
  "message": "회원가입 실패 : [에러 사유]"
}
```
  </details>
</details>


<details markdown="1">
<summary>로그인 `POST /users/login`</summary>
 
#### Ruquest Body : 
```json
{
  "memberId": "string",
  "memberPw": "string"
}
```
 
<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
"status": 200,
  "message": "JWT token"
}
```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json

{
"status": 404,
  "message": "존재하지 않는 회원입니다."
}
```

```json

{
"status": 401,
"message": "로그인 실패 : 비밀번호가 일치하지 않습니다."
}
```
  </details>
</details>


<details markdown="1">
<summary>내정보 조회 `GET /users/me`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`

 
<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
"status": 200,
"message": "<memberId> 님"
}
```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json

{
  "error": "Invalid or missing JWT token"
}
```

  </details>
</details>

<details markdown="1">
<summary>내정보 수정 `PUT /users/me`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`

 #### Ruquest Body : 
```json
{
  "memberId": "string",
  "memberPw": "string"
}
```

<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
"status": 200,
"message": "회원 정보가 성공적으로 수정되었습니다."
}
```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json

// 필수 필드 누락
{
  "status": 400,
  "message": "[에러 사유]"
}

// 토큰 없음/유효하지 않을 때
{
  "error": "Invalid or missing JWT token"
}

```

  </details>
</details>

<details markdown="1">
<summary>회원 탈퇴 `DELETE /users/me`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`

<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
"status": 200,
"message": "회원이 성공적으로 탈퇴되었습니다."
}
```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json

{
  "error": "Invalid or missing JWT token"
}

```

  </details>
</details>

<br>
 
### TODO API

<details markdown="1">
<summary>TODO 생성 `POST /todos`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`

 #### Ruquest Body : 
```json
{
  "todoTitle": "string",
  "todoContent": "string"
}
```

<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
  "status": 201,
  "message": "TODO가 성공적으로 등록되었습니다."
}

```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json
// 인증 실패
{
  "error": "Invalid or missing JWT token"
}

// 필드 검증 실패
{
  "status": 400,
  "message": "[에러 사유]"
}

```
  </details>
</details>


<details markdown="1">
<summary>TODO 전체 목록 조회 `GET /todos`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`


<details markdown="1">
  <summary>Success Response</summary>
  
```json

  {
    "todoNo": "string",
    "memberNo": "string",
    "todoTitle": "string",
    "todoContent": "string",
    "completed": 0,
    "completedStatus": "미완료",
    "instDtm": "2025-05-14 12:00:00",
    "updateDtm": null
  }



```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json
// 인증 실패
{
  "error": "Invalid or missing JWT token"
}

```
  </details>
</details>



<details markdown="1">
<summary>TODO 수정 `PUT /todos/{id}`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`

 #### Ruquest Body : 
```json
{
  "todoTitle": "string", 
  "todoContent": "string",
  "completed": 1
}

```
<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
  "status": 200,
  "message": "TODO가 성공적으로 수정되었습니다."
}


```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json
// 인증 실패
{"error":"Invalid or missing JWT token"}
// 필드 검증 실패
{"status":400,"message":"[에러 사유]"}
// 존재하지 않는 id
{"status":404,"message":"TODO를 찾을 수 없습니다."}

```
  </details>
</details>


<details markdown="1">
<summary>TODO 삭제 `DELETE /todos/{id}`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`

<details markdown="1">
  <summary>Success Response</summary>
  
```json

{
  "status": 200,
  "message": "TODO가 성공적으로 삭제되었습니다."
}


```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json
// 인증 실패
{"error":"Invalid or missing JWT token"}
// 존재하지 않는 id
{"status":404,"message":"TODO를 찾을 수 없습니다."}


```
  </details>
</details>


<details markdown="1">
<summary>TODO 검색 `GET /todos/search`</summary>

  #### Headers : `Authorization: Bearer <JWT 토큰>`

<details markdown="1">
  <summary>Success Response</summary>
  
```json

[
  { /* Todo 객체 배열 구조 */ }
]


```
  </details>
   <details markdown="1">
  <summary>Error Response</summary>
  
```json
{"error":"Invalid or missing JWT token"}

```
  </details>
</details>
