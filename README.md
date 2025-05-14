# JWT기반 TODO 백엔드 API구현

---

## 🧑‍💻 실행 방법

### 전제 조건
- java 17.0.15
- Maven
- SQLite (기본 내장 DB 파일로 동작)

### 빌드 & 실행 (Maven 기준)
- application.properties에서 SQLite 파일 경로 및 JWT 시크릿을 설정합니다.

---

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
<br>
 
