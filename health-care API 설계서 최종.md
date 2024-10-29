<h1 style='background-color: rgba(55, 55, 55, 0.4); text-align: center'>Health_care API 설계(명세)서</h1>
해당 API 명세서는 '헬스케어 ERP - health-care'의 REST API를 명세하고 있습니다.

- Domain : <http://localhost:4000>

---

<h2 style='background-color: rgba(55, 55, 55, 0.2); text-align: center'>Auth 모듈</h2>

healthcare 서비스의 인증 및 인가와 관련된 REST API 모듈입니다.  
로그인, 회원가입, 소셜 로그인 등의 API가 포함되어 있습니다.  
Auth 모듈은 인증 없이 요청할 수 있습니다.

- url : /api/v1/auth

---

#### - 로그인

##### - 설명

클라이언트는 사용자 아이디와 평민의 비밀번호를 입력하여 요청하고 아이디와 비밀번호가 일치한다면 인증에 사용될 token과 해당 token의 만료 기간을 응답 데이터로 전달받습니다. 만약 아이디 혹은 비밀번호가 하나라도 틀린다면 로그인 정보 불일치에 해당하는 응답을 받게 됩니다. 네트워크 에러, 서버 에러, 데이터베이스 에러, 토큰 생성 에러가 발생할 수 있습니다.

- method : **POST**
- end point : **/sign-in**

##### Request

###### Request Body

| name     |  type  |    description    | required |
| -------- | :----: | :---------------: | :------: |
| userId   | String |  사용자의 아이디  |    O     |
| password | String | 사용자의 비밀번호 |    O     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/sign-in" \
 -d "userId=qwer1234" \
 -d "password=qwer1234"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name        |  type   |             description             | required |
| ----------- | :-----: | :---------------------------------: | :------: |
| code        | String  |              결과 코드              |    O     |
| message     | String  |        결과 코드에 대한 설명        |    O     |
| accessToken | String  | Bearer token 인증 방식에 사용될 JWT |    O     |
| expiration  | Integer |       JWT 만료 기간 (초단위)        |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
  "accessToken": "${ACCESS_TOKEN}",
  "expiration": 32400
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 실패 (로그인 정보 불일치)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "SF",
  "message": "Sign in failed."
}
```

**응답 실패 (토큰 생성 실패)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "TCF",
  "message": "Token creation failed."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 아이디 중복 확인

##### 설명

클라이언트는 사용할 아이디를 입력하여 요청하고 중복되지 않는 아이디라면 성공 응답을 받습니다. 만약 아이디가 중복된다면 아이디 중복에 해당하는 응답을 받게 됩니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **POST**
- end point : **/id-check**

##### Request

###### Request Body

| name   |  type  |         description         | required |
| ------ | :----: | :-------------------------: | :------: |
| userId | String | 중복확인 할 사용자의 아이디 |    O     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/id-check" \
 -d "userId=qwer1234"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (중복된 아이디)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "DI",
  "message": "Duplicated user id."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

---

#### - 닉네임 중복 확인

클라이언트는 사용할 닉네임을 입력하여 요청하고 중복되지 않는 닉네임이라면 성공 응답을 받습니다.
만약 아이디가 중복된다면 닉네임 중복에 해당하는 응답을 받게 됩니다.
네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **POST**
- end point : **/nickname-check**

##### Request

###### Request Body

| name     |  type  |         description         | required |
| -------- | :----: | :-------------------------: | :------: |
| nickname | String | 중복확인 할 사용자의 닉네임 |    O     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/nickname-check" \
 -d "nickname=뽀삐puppy12"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (중복된 닉네임)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "DN",
  "message": "Duplicated user nickname."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

---

#### - 전화번호 인증

##### 설명

클라이언트는 숫자로만 이루어진 11자리 전화번호를 입력하여 요청하고 이미 사용 중인 전화번호인지 확인 후 4자리의 인증번호를 해당 전화번호에 문자를 전송합니다. 인증번호가 정상적으로 전송이 된다면 성공 응답을 받습니다. 만약 중복된 전화번호를 입력한다면 중복된 전화번호에 해당하는 응답을 받게 됩니다. 네트워크 에러, 서버 에러, 데이터베이스 에러, 문자 전송 실패가 발생할 수 있습니다.

- method : **POST**
- end point : **/tel-auth**

##### Request

###### Request Body

| name      |  type  |                    description                     | required |
| --------- | :----: | :------------------------------------------------: | :------: |
| telNumber | String | 인증 번호를 전송할 사용자의 전화번호 (11자리 숫자) |    O     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/tel-auth" \
 -d "telNumber=01011112222"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (중복된 전화번호)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "DT",
  "message": "Duplicated user tel number."
}
```

**응답 실패 (인증번호 전송 실패)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "TF",
  "message": "Auth number send failed."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

---

#### - 인증번호 확인

##### 설명

클라이언트는 사용자 전화번호와 인증번호를 입력하여 요청하고 해당하는 전화번호와 인증번호가 서로 일치하는지 확인합니다. 일치한다면 성공에 대한 응답을 받습니다. 만약 일치하지 않는다면 전화번호 인증 실패에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **POST**
- end point : **/tel-auth-check**

##### Request

###### Request Body

| name       |  type  |            description             | required |
| ---------- | :----: | :--------------------------------: | :------: |
| telNumber  | String | 인증 번호를 확인할 사용자 전화번호 |    O     |
| authNumber | String |    인증 확인에 사용할 인증 번호    |    O     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/tel-auth-check" \
 -d "telNumber=01011112222" \
 -d "authNumber=1234"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (전화번호 인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "TAF",
  "message": "Tel number authentication failed."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

---

#### - 회원가입

##### 설명

클라이언트는 사용자 이름, 사용자 아이디, 사용자 닉네임, 비밀번호, 전화번호, 인증번호, 가입 경로, 프로필 이미지, 개인 목표, 키, 몸무게, 골격근량, 체지방량, 벤치프레스, 데드리프트, 스쿼트, 개인 목표를 입력하여 요청하고 회원가입이 성공적으로 이루어지면 성공에 대한 응답을 받습니다. 만약 존재하는 아이디, 닉네임일 경우 중복된 아이디, 닉네임에 대한 응답을 받고,
만약 존재하는 전화번호일 경우 중복된 전화번호에 대한 응답을 받고, 전화번호와 인증번호가 일치하지 않으면 전화번호 인증 실패에 대한 응답을 받습니다. 네트워크에

- method : **POST**
- end point : **/sign-up**

##### Request

###### Request Body

| name               |  type  |                          description                           | required |
| ------------------ | :----: | :------------------------------------------------------------: | :------: |
| profileImage       | String |                      사용자 프로필이미지                       |    X     |
| name               | String |                         사용자의 이름                          |    O     |
| userId             | String |                        사용자의 아이디                         |    O     |
| nickname           | String |                        사용자의 닉네임                         |    O     |
| password           | String |            사용자의 비밀번호 (8~13자의 영문 + 숫자)            |    O     |
| telNumber          | String |                사용자의 전화번호 (11자의 숫자)                 |    O     |
| authNumber         | String |                       전화번호 인증번호                        |    O     |
| joinPath           | String | 회원가입 경로 (기본: 'HOME', 카카오: 'KAKAO', 네이버: 'NAVER') |    O     |
| snsId              | String |                    SNS 가입시 sns oauth2 ID                    |    X     |
| height             | Float  |                           사용자 키                            |    O     |
| weight             | Float  |                         사용자 몸무게                          |    O     |
| skeletalMuscleMass | Float  |                            골격근량                            |    X     |
| bodyFatMass        | Float  |                        사용자 체지방량                         |    X     |
| deadlift           | Float  |                   사용자 3대 측정 데드리프트                   |    X     |
| benchPress         | Float  |                   사용자 3대 측정 벤치프레스                   |    X     |
| squat              | Float  |                     사용자 3대 측정 스쿼트                     |    X     |
| personalGoal       | String |                        사용자 개인목표                         |    X     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/sign-up" \
 -d "profileImage=null"\
 -d "name=홍길동"\
 -d "userId=qwer1234"\
 -d "nickname=뽀삐puppy12"\
 -d "password=qwer1234"\
 -d "telNumber=01011112222"\
 -d "authNumber=1234"\
 -d "joinPath=HOME"\
 -d "height=180"\
 -d "weigh=80"\
 -d "skeletalMuscleMass=34.5"\
 -d "bodyFatMass=10.4"\
 -d "skeletalMuscleMass=34.5"\
 -d "deadlift=124"\
 -d "benchPress=74"\
 -d "squat=100"\
 -d "personalGoal=이번달은 3대 측정 500 만들꺼야"\
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (중복된 아이디)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "DI",
  "message": "Duplicated user id."
}
```

**응답 : 실패 (중복된 닉네임)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "DN",
  "message": "Duplicated user nickname."
}
```

**응답 : 실패 (중복된 전화번호)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "DT",
  "message": "Duplicated user tel number."
}

```

**응답 : 실패 (전화번호 인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "TAF",
  "message": "Tel number authentication failed."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

---

#### - SNS 회원가입 및 로그인

##### 설명

클라이언트는 OAuth 인증서버를 입력하여 요청하고 해당하는 Redirect 응답을 받습니다. 회원가입이 되어있는 사용자의 경우 쿼리 매개변수로 접근 토큰과 토큰 만료 기간을 반환하며 회원가입이 되어있지 않은 사용자의 경우 쿼리 매개변수로 sns 아이디와 해당하는 sns 서비스의 이름을 반환합니다.

- method : **GET**
- end point : **/sns-sign-in/{registerId}**

##### Request

###### Path Variable

| name       |  type  |                 description                 | required |
| ---------- | :----: | :-----------------------------------------: | :------: |
| registerId | String | 사용 SNS (카카오: 'kakao', 네이버: 'naver') |    O     |

###### Example

```bash
curl -X POST "http://localhost:4000/api/v1/auth/sns-sign-in/{kakao}"
```

##### Response

###### Example

**응답 성공 (회원 O)**

```bash
HTTP/1.1 302 Found
Location: http://localhost:3000/sns-success?accessToken=${accessToken}&expiration=36000
```

**응답 성공 (회원 X)**

```bash
HTTP/1.1 302 Found
Location: http://localhost:3000/auth?snsId=${snsId}&joinPath=${joinPath}
```

---

<h2 style='background-color: rgba(55, 55, 55, 0.2); text-align: center'>Customer 모듈</h2>

healthcare 서비스에 사용자와 관련된 REST API 모듈입니다.
사용자 정보확인, 정보수정, 3대측정 등록, 신체변화 등록 등의 API가 포함되어 있습니다.
User 모듈은 모두 인증이 필요합니다.

- url : /api/v1/customer

---

#### - 사용자 신체 정보 보기

##### 설명

사용자는 요청 헤더에 Bearer 인증 토큰을 포함하고 URL에 사용자 아이디를 포함하여 요청하고 성공적으로 이루어지면 사용자 신체정보 번호, 사용자의 몸무게, 골격근량, 체지방량, 사용자 신체 정보 등록 날짜를 응답받습니다. 만약 존재하지 않는 아이디일 경우 존재하지 않는 아이디에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**
- end point : **/{userId}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/customer/qwer1234" \
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name                   |  type   |        description         | required |
| ---------------------- | :-----: | :------------------------: | :------: |
| code                   | String  |         결과 코드          |    O     |
| message                | String  |   결과 코드에 대한 설명    |    O     |
| user_muscle_fat_number | Integer |   사용자 신체 정보 번호    |    O     |
| userId                 | String  |       사용자 아이디        |    O     |
| weight                 |  Float  |           몸무게           |    O     |
| skeletal_muscle_mass   |  Float  |          골격근량          |    X     |
| body_fat_mass          |  Float  |          체지방량          |    X     |
| user_muscle_fat_date   |  Date   | 사용자 신체 정보 등록 날짜 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
  "userId": "qwer1234",
  "weigh" : 80 ,
  "skeletalMuscleMass" : 34.5,
  "bodyFatMass" : 10.4
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

---

#### - 사용자 3대 측정 정보

##### 설명

사용자는 요청 헤더에 Bearer 인증 토큰을 포함하고 URL에 사용자 아이디를 포함하여 요청하고 성공적으로 이루어지면 사용자 3대 측정 정보 번호, 데드리프트, 벤치프레스, 스쿼트, 사용자 3대 측정 정보 등록 날짜를 응답받습니다. 만약 존재하지 않는 아이디일 경우 존재하지 않는 아이디에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**
- end point : **/{userId}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/customer/qwer1234"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name                              |  type   |          description           | required |
| --------------------------------- | :-----: | :----------------------------: | :------: |
| code                              | String  |           결과 코드            |    O     |
| message                           | String  |     결과 코드에 대한 설명      |    O     |
| health_machine_measurement_number | Integer |   사용자 3대 측정 정보 번호    |    O     |
| userId                            | String  |         사용자 아이디          |    O     |
| deadlift                          |  Float  |         데드리프트(kg)         |    X     |
| bench_press                       |  Float  |         벤치프레스(kg)         |    X     |
| squat                             |  Float  |             스쿼트             |    X     |
| health_machine_measurement_date   |  Date   | 사용자 3대 측정 정보 등록 날짜 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
  "userId": "qwer1234",
  "weigh" : 80 ,
  "skeletalMuscleMass" : 34.5,
  "bodyFatMass" : 10.4
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

---

#### - 마이페이지 정보 수정

- method : **PATCH**
- end point : **/{userId}**

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 사용자 프로필 이미지, 이름, 닉네임, 키, 개인 목표, 몸무게, 골격근량, 체지방량, 벤치프레스, 스쿼트, 데드리프트를 입력하여 요청하고 마이페이지 정보 수정이 성공적으로 이루어지면 성공에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **PATCH**
- end point : **/**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name               |  type  |        description         | required |
| ------------------ | :----: | :------------------------: | :------: |
| profileImage       | String |    사용자 프로필이미지     |    X     |
| name               | String |       사용자의 이름        |    O     |
| nickname           | String |      사용자의 닉네임       |    O     |
| height             | Float  |         사용자 키          |    O     |
| weight             | Float  |       사용자 몸무게        |    O     |
| skeletalMuscleMass | Float  |          골격근량          |    X     |
| bodyFatMass        | Float  |      사용자 체지방량       |    X     |
| deadlift           | Float  | 사용자 3대 측정 데드리프트 |    X     |
| benchPress         | Float  | 사용자 3대 측정 벤치프레스 |    X     |
| squat              | Float  |   사용자 3대 측정 스쿼트   |    X     |
| personalGoal       | String |      사용자 개인목표       |    X     |

###### Example

```bash
curl -v -X PATCH "http://localhost:4000/api/v1" \
 -d "profileImage=null"\
 -d "name=홍길동"\
 -d "nickname=뽀삐puppy12"\
 -d "height=180"\
 -d "weigh=80"\
 -d "skeletalMuscleMass=34.5"\
 -d "bodyFatMass=10.4"\
 -d "skeletalMuscleMass=34.5"\
 -d "deadlift=124"\
 -d "benchPress=74"\
 -d "squat=100"\
 -d "personalGoal=이번달은 3대 측정 500 만들꺼야"\
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

<h2 style='background-color: rgba(55, 55, 55, 0.2); text-align: center'>Board 모듈</h2>

모든 클라이언트는 게시물 번호, 게시물 제목, 게시물 작성자 닉네임, 게시물 작성 날짜, 게시물 조회 수가 조회가 되면 성공적으로 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- url : /api/v1/board

---

#### - 게시물 리스트 보기

##### 설명

모든 클라이언트는 게시물 번호, 게시물 제목, 게시물 작성자 닉네임, 게시물 작성날짜, 게시물 조회수가 조회가 되면 성공적으로 응답을 받습니다. 네트워크 에러, 서버에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**
- end point : **/**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    X     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/board" \

```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type   |      description      | required |
| ------- | :-----: | :-------------------: | :------: |
| code    | String  |       결과 코드       |    O     |
| message | String  | 결과 코드에 대한 설명 |    O     |
| boards  | Board[] |     게시글 리스트     |    O     |

**Board**  
| name | type | description | required |
|---|:---:|:---:|:---:|
| board_number | String | 게시물 번호 | O |
| board_title | String | 게시물 제목 | O |
| nickname | String | 게시물 작성자 닉네임 | O |
| board_upload_date | String | 게시물 작성날짜 | O |
| board_view_count | String | 게시물 조회수 | O |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
  "tools": [
    {
      "boardNumber": 1,
      "boardTitle": "오늘에 추천 식단은~~",
      "nickName": "뽀보이strong1",
      "board_upload_date": 2024-10-17 14:36,
      "board_view_count" : 20
    },
    ...
  ]
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 게시물 등록

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 게시물 제목, 유저 닉네임, 카테고리, 태그, 내용, 유튜브 비디오 링크, 게시물 자료를 입력하여 요청하고 게시물 등록이 성공적으로 이루어지면 성공에 대한 응답을 받습니다. 카테고리와 태그는 중복 선택이 되지 않습니다. 헬스장 지도는 헬스장 카테고리를 선택했을 때만 나타납니다. 네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **POST**
- end point : **/**

#### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name              |  type  |    description    | required |
| ----------------- | :----: | :---------------: | :------: |
| boardTitle        | String |    게시물 제목    |    O     |
| boardCategory     | String |  게시물 카테고리  |    O     |
| boardTag          | String |    게시물 태그    |    O     |
| boardContents     | String |    게시물 내용    |    O     |
| youtubeVideoLink  | String | 유튜브비디오 링크 |    X     |
| boardFileContents | String |    게시물 자료    |    X     |
| mapLat            | Float  |       위도        |    X     |
| mapLng            | Float  |       경도        |    X     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/board" \
 -h "Authorization=Bearer XXXX" \
 -d "boardTitle=오늘은 다이어트 20일차" \
 -d "boardCategory=운동일지", \
 -d "boardTag=운동", \
 -d "youtubeVideoLink=null", \
 -d "boardFileContents=null", \
 -d "mapLat=37.5691", \
 -d "mapLng=126.9786",
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 게시물 상세 페이지

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하지 않아도 응답을 받습니다. Bearer 인증 토큰과 URL에 용품 번호를 포함하고 요청하여 조회가 성공적으로 이루어지면 로그인한 게시물 상세 페이지를 응답받습니다.
네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**
- end point : **/{boardNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    X     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/board/1" \
 -h "Authorization=Bearer XXXX"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name             |   type    |      description      | required |
| ---------------- | :-------: | :-------------------: | :------: |
| code             |  String   |       결과 코드       |    O     |
| message          |  String   | 결과 코드에 대한 설명 |    O     |
| bordNumber       |  Integer  |      게시물 번호      |    O     |
| boardTitle       |  String   |      게시물 제목      |    O     |
| nickName         |  String   |     게시물 닉네임     |    O     |
| boardUploadDate  |   Date    | 작성 게시물 생성 날짜 |    O     |
| boardContents    |  String   |      게시물 내용      |    O     |
| youtubeVideoLink |  String   |  유튜브 비디오 링크   |    X     |
| boardViewCount   |  Integer  |        조회수         |    O     |
| boardLikeCount   |  Integer  |    게시물 추천 수     |    O     |
| comments         | Cooment[] |      댓글 리스트      |    O     |

**comment**
| name | type | description | required |
|---|:---:|:---:|:---:|
| commentsNumber | Integer | 댓글 번호 | O |
| userId | String | 댓글 사용자 아이디 | O |
| commentsContents | String | 댓글 내용 | O |
| commentsLikeCount | Integer | 댓글 추천수 |O |
| commentsDate | Date | 댓글 작성 날짜 | O |

###### Example

**응답 성공**

````bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
  "bordNumber": 1,
  "nickName": "뽀보이strong1",
  "boardUploadDate": 2024-10-17 14:36,
  "boardContents": "오늘 하체랑 엉덩이가 터질것같다"
  "youtubeVideoLink": null,
  "boardViewCount": 10,
  "boardLikeCount": 10,
  "commentsNumber": 2
}

```

**응답 실패 (데이터 유효성 검사 실패)**
```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
````

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 게시물 수정

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고
URL에 게시물 번호를 포함하고 게시물 번호, 게시물 제목, 게시물 닉네임, 작성 게시물 생성 날짜, 게시물 내용, 유튜브 비디오 링크,
게시물 파일 번호, 지도 경도, 위도를 입력하여 요청하고 게시물 수정이 성공적으로 이루어지면 성공에 대한 응답을 받습니다.
네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **PATCH**
- end point : **/{boardNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name              |  type  |    description    | required |
| ----------------- | :----: | :---------------: | :------: |
| boardTitle        | String |   게시물 타이틀   |    O     |
| boardCategory     | String |  게시물 카테고리  |    O     |
| boardTag          | String |    게시물 태그    |    O     |
| boardContents     | String |    게시물 내용    |    O     |
| youtubeVideoLink  | String | 유튜브비디오 링크 |    X     |
| boardFileContents | String |    게시물 자료    |    X     |
| mapLat            | Float  |       위도        |    X     |
| mapLng            | Float  |       경도        |    X     |

###### Example

```bash
curl -v -X PATCH "http://localhost:4000/api/v1/boardNumber/1" \
 -h "Authorization=Bearer XXXX" \
 -d "boardTitle=오늘은 다이어트 94일차" \
 -d "boardCategory=식단일지", \
 -d "boardTag=식단", \
 -d "youtubeVideoLink=null", \
 -d "boardFileContents=null", \
 -d "mapLat=34.5691", \
 -d "mapLng=106.9786",
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

##### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 게시물 삭제

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고
URL에 게시물 번호를 포함하고 게시물 삭제가 성공적으로 이루어진다면 성공에 대한 응답을 받습니다.
네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **DELETE**
- end point : **/{boardNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Example

```bash
curl -v -X DELETE "http://localhost:4000/api/v1/board/1" \
 -h "Authorization=Bearer XXXX"
```

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 댓글 등록

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 댓글 내용을 입력하여 요청하고 댓글 등록이 성공적으로 이루어지면 성공에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **POST**
- end point : **/**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name          |  type  | description | required |
| ------------- | :----: | :---------: | :------: |
| boardContents | String |  댓글 내용  |    O     |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/board/3" \
 -h "Authorization=Bearer XXXX" \
 -d "boardContents=너무 도움됬어요 감사함욤"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 댓글 수정

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 URL에 댓글 번호를 포함하고 댓글 내용을 입력하여 요청하고 댓글 수정이 성공적으로 이루어지면 성공에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **PATCH**
- end point : **/{commentNumber}**

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name          |  type  | description | required |
| ------------- | :----: | :---------: | :------: |
| boardContents | String |  댓글 내용  |    O     |

###### Example

```bash
curl -v -X PATCH "http://localhost:4000/api/v1/comment/1" \
 -h "Authorization=Bearer XXXX" \
 -d "boardContent=제 친구랑 같이 이 방법으로 운동중이에요!!"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 댓글 삭제

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 URL에 댓글 번호를 포함하여 요청하고 댓글 삭제가 성공적으로 이루어지면 성공에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **DELETE**
- end point : **/{commentsNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

```bash
curl -v -X DELETE "http://localhost:4000/api/v1/comment/1" \
 -h "Authorization=Bearer XXXX"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success."
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

<h2 style='background-color: rgba(55, 55, 55, 0.2); text-align: center'> Calendar 모듈</h2>

Healthcare 서비스의 스케줄표와 관련된 REST API 모듈입니다.  
운동 및 식단의 스케줄에 일정을 등록할 수 있습니다.  
운동 및 식단의 스케줄이 작성되어있는 달력을 볼 수 있습니다.  
식단에 대한 정보는 외부 API를 받아와 사용합니다.  
스케줄표의 일정 등록 및 수정은 인증이 필요합니다.  
외부 API인 [FullCalendar API]를 가져와서 사용합니다.

[FullCalendar API]: https://fullcalendar.io/docs/react/

##### 식단 스케줄: - url : /api/v1/health-schedule

##### 운동 스케줄: - url : /api/v1/meal-schedule

---

#### - 운동 스케줄표 일정 등록

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 달력의 날짜를 클릭하여 운동 스케줄을 작성 후 등록합니다. 등록이 된다면 성공에 대한 응답을 받습니다. 등록이 되지 않는다면 네트워크 에러, 서버 에러가 발생할 수 있습니다.

- method : **POST**
- end point : **/**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name                 |  type   |          description          | required |
| -------------------- | :-----: | :---------------------------: | :------: |
| healthScheduleNumber | Integer |       운동 스케줄 번호        |    O     |
| userId               | String  |         사용자 아이디         |    O     |
| healthTitle          | String  |           일정 제목           |    O     |
| healthMemo           | String  |           일정 내용           |    O     |
| healthScheduleStart  |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| healthScheduleEnd    |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/health-schedule" \
-h "Authorization=Bearer XXXX" \
-d "health_schedule_number=1" \
-d "health_title=가슴" \
-d "health_memo=
      벤치프레스 12 3set,
      플라이 15 5set"
-d "schedule_start=2024.10.17 12:00"
-d "schedule_end=2024.10.17 23:59"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 식단 스케줄표 일정 등록

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 달력의 날짜를 클릭하여 식단 스케줄을 작성 후 등록합니다. 등록이 된다면 성공에 대한 응답을 받습니다. 등록이 되지 않는다면 네트워크 에러, 서버 에러가 발생할 수 있습니다. 식품에 대한 정보는 외부 API를 받아와 사용합니다.

- method : **POST**
- end point : **/**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name               |  type   |          description          | required |
| ------------------ | :-----: | :---------------------------: | :------: |
| mealScheduleNumber | Integer |       식단 스케줄 번호        |    O     |
| userId             | String  |         사용자 아이디         |    O     |
| mealTitle          | String  |           일정 제목           |    O     |
| mealMemo           | String  |     일정 내용(식품 정보)      |    O     |
| mealScheduleStart  |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| mealScheduleEnd    |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/meal-schedule" \
-h "Authorization=Bearer XXXX" \
-d "health_schedule_number=1" \
-d "health_title=아침" \
-d "health_memo=
      닭가슴살 109kcal,
      사과 52kcal"
-d "schedule_start=2024.10.17 12:00"
-d "schedule_end=2024.10.17 23:59"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 운동 스케줄표 상세 일정

##### 설명

클라이언트는 외부 API의 달력을 불러와 운동 스케줄을 확인할 수 있습니다. 등록과 수정이 가능합니다. 스케줄의 확인이 된다면 성공에 대한 응답을 받습니다. 스케줄의 확인이 되지 않는다면 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**
- end point : **/{healthScheduleNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    X     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/health-schedule/1" \

```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name                 |  type   |          description          | required |
| -------------------- | :-----: | :---------------------------: | :------: |
| code                 | String  |           결과 코드           |    O     |
| message              | String  |     결과 코드에 대한 설명     |    O     |
| healthScheduleNumber | Integer |       운동 스케줄 번호        |    O     |
| userId               | String  |         사용자 아이디         |    O     |
| healthTitle          | String  |           일정 제목           |    O     |
| healthMemo           | String  |           일정 내용           |    O     |
| healthScheduleStart  |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| healthScheduleEnd    |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 식단 스케줄표 상제 일정

##### 설명

클라이언트는 외부 API의 달력을 불러와 식단 스케줄을 확인할 수 있습니다. 등록과 수정이 가능합니다. 스케줄의 확인이 된다면 성공에 대한 응답을 받습니다. 스케줄의 확인이 되지 않는다면 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**
- end point : **/{mealScheduleNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    X     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/meal-schedule/1" \

```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name               |  type   |          description          | required |
| ------------------ | :-----: | :---------------------------: | :------: |
| code               | String  |           결과 코드           |    O     |
| message            | String  |     결과 코드에 대한 설명     |    O     |
| mealScheduleNumber | Integer |       식단 스케줄 번호        |    O     |
| userId             | String  |         사용자 아이디         |    O     |
| mealTitle          | String  |           일정 제목           |    O     |
| mealMemo           | String  |     일정 내용(식품 정보)      |    O     |
| scheduleStart      |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| scheduleEnd        |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 운동 스케줄표 일정 수정

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 달력의 날짜에 등록이 된 운동 스케줄을 클릭하여 수정이 된다면 성공에 대한 응답을 받습니다. 수정이 되지 않는다면 네트워크 에러, 서버 에러가 발생할 수 있습니다.

- method : **PATCH**
- end point : **/{healthScheduleNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name                 |  type   |          description          | required |
| -------------------- | :-----: | :---------------------------: | :------: |
| healthScheduleNumber | Integer |       운동 스케줄 번호        |    O     |
| userId               | String  |         사용자 아이디         |    O     |
| healthTitle          | String  |           일정 제목           |    O     |
| healthMemo           | String  |           일정 내용           |    O     |
| healthScheduleStart  |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| healthScheduleEnd    |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/healthScheduleNumber/1" \
-h "Authorization=Bearer XXXX" \
-d "health_schedule_number=1" \
-d "health_title=가슴, 이두" \
-d "health_memo=
      벤치프레스 12 3set,
      체스트 프레스 15 5set,
      플라이 15 5set,
      바벨 컬 10 3set,
      덤벨 컬 10 3set"
-d "schedule_start=2024.10.17 12:00"
-d "schedule_end=2024.10.17 23:59"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 식단 스케줄표 일정 수정

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 달력의 날짜에 등록이 된 식단 스케줄을 클릭하여 수정이 된다면 성공에 대한 응답을 받습니다. 수정이 되지 않는다면 네트워크 에러, 서버 에러가 발생할 수 있습니다. 식품에 대한 정보는 외부 API를 받아와 사용합니다.

- method : **PATCH**
- end point : **/{mealScheduleNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name               |  type   |          description          | required |
| ------------------ | :-----: | :---------------------------: | :------: |
| mealScheduleNumber | Integer |       식단 스케줄 번호        |    O     |
| userId             | String  |         사용자 아이디         |    O     |
| mealTitle          | String  |           일정 제목           |    O     |
| mealMemo           | String  |     일정 내용(식품 정보)      |    O     |
| scheduleStart      |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| scheduleEnd        |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/mealScheduleNumber/1" \
-h "Authorization=Bearer XXXX" \
-d "health_schedule_number=1" \
-d "health_title=아침" \
-d "health_memo=
      닭가슴살 109kcal,
      사과 52kcal,
      현미밥 152kcal"
-d "schedule_start=2024.10.17 12:00"
-d "schedule_end=2024.10.17 23:59"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 운동 스케줄표 일정 삭제

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 운동 스케줄의 번호를 포함하고 일정을 삭제합니다. 삭제가 된다면 성공에 대한 응답을 받습니다. 삭제가 되지 않는다면 네트워크 에러, 서버 에러가 발생할 수 있습니다.

- method : **DELETE**
- end point : **/{healthScheduleNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name                 |  type   |          description          | required |
| -------------------- | :-----: | :---------------------------: | :------: |
| healthScheduleNumber | Integer |       운동 스케줄 번호        |    O     |
| userId               | String  |         사용자 아이디         |    O     |
| healthTitle          | String  |           일정 제목           |    O     |
| healthMemo           | String  |           일정 내용           |    O     |
| healthScheduleStart  |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| healthScheduleEnd    |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/health-schedule/1" \
  -h "Authorization=Bearer XXXX" \
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```

#### - 식단 스케줄표 일정 삭제

##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하고 식단 스케줄의 번호를 포함하고 일정을 삭제합니다. 삭제가 된다면 성공에 대한 응답을 받습니다. 삭제가 되지 않는다면 네트워크 에러, 서버 에러가 발생할 수 있습니다.

- method : **DELETE**
- end point : **/{mealScheduleNumber}**

##### Request

###### Header

| name          |      description      | required |
| ------------- | :-------------------: | :------: |
| Authorization | Bearer 토큰 인증 헤더 |    O     |

###### Request Body

| name               |  type   |          description          | required |
| ------------------ | :-----: | :---------------------------: | :------: |
| mealScheduleNumber | Integer |       식단 스케줄 번호        |    O     |
| userId             | String  |         사용자 아이디         |    O     |
| mealTitle          | String  |           일정 제목           |    O     |
| mealMemo           | String  |     일정 내용(식품 정보)      |    O     |
| scheduleStart      |  Date   |  스케줄 등록을 위한 시작날짜  |    O     |
| scheduleEnd        |  Date   | 스케줄 등록을 위한 마지막날짜 |    O     |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/meal-schedule/1" \
  -h "Authorization=Bearer XXXX"
```

##### Response

###### Header

| name         |                       description                        | required |
| ------------ | :------------------------------------------------------: | :------: |
| Content-Type | 반환되는 Response Body의 Content type (application/json) |    O     |

###### Response Body

| name    |  type  |      description      | required |
| ------- | :----: | :-------------------: | :------: |
| code    | String |       결과 코드       |    O     |
| message | String | 결과 코드에 대한 설명 |    O     |

###### Example

**응답 성공**

```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
}
```

**응답 실패 (데이터 유효성 검사 실패)**

```bash
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "code": "VF",
  "message": "Validation failed."
}
```

**응답 : 실패 (인증 실패)**

```bash
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "code": "AF",
  "message": "Authentication fail."
}
```

**응답 실패 (데이터베이스 에러)**

```bash
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8

{
  "code": "DBE",
  "message": "Database error."
}
```
