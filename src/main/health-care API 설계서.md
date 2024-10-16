<h1 style='background-color: rgba(55, 55, 55, 0.4); text-align: center'>Health_care API 설계(명세)서</h1>
해당 API 명세서는 '헬스케어 ERP - health-care'의 REST API를 명세하고 있습니다. 

- Domain : <http://localhost:4000> 

***

<h2 style='background-color: rgba(55, 55, 55, 0.2); text-align: center'>Auth 모듈</h2>

healthcare 서비스의 인증 및 인가와 관련된 REST API 모듈입니다.  
로그인, 회원가입, 소셜 로그인 등의 API가 포함되어 있습니다.  
Auth 모듈은 인증 없이 요청할 수 있습니다.  
  
- url : /api/v1/auth  

***

#### - 로그인

##### - 설명

클라이언트는 사용자 아이디와 평문의 비밀번호를 입력하여 요청하고 아이디와 비밀번호가 일치한다면 인증에 사용될 token과 해당 token의 만료 기간을 응답 데이터로 전달 받습니다. 만약 아이디 혹은 비밀번호가 하나라도 틀린다면 로그인 정보 불일치에 해당하는 응답을 받게됩니다. 네트워크 에러, 서버 에러, 데이터베이스 에러, 토큰 생성 에러가 발생할 수 있습니다.

- method : **POST**  
- end point : **/sign-in**

##### Request

###### Request Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| userId | String | 사용자의 아이디 | O |
| password | String | 사용자의 비밀번호 | O |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/sign-in" \
 -d "userId=qwer1234" \
 -d "password=qwer1234"
```

##### Response

###### Header

| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |
| accessToken | String | Bearer token 인증 방식에 사용될 JWT | O |
| expiration | Integer | JWT 만료 기간 (초단위) | O |

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

클라이언트는 사용할 아이디를 입력하여 요청하고 중복되지 않는 아이디라면 성공 응답을 받습니다. 만약 아이디가 중복된다면 아이디 중복에 해당하는 응답을 받게됩니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다. 

- method : **POST**  
- end point : **/id-check**  

##### Request

###### Request Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| userId | String | 중복확인 할 사용자의 아이디 | O |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/id-check" \
 -d "userId=qwer1234"
```

##### Response

###### Header

| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |

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

***

#### - 닉네임 중복 확인

클라이언트는 사용할 닉네임을 입력하여 요청하고 중복되지 않는 닉네임이라면 성공 응답을 받습니다.
만약 아이디가 중복된다면 닉네임 중복에 해당하는 응답을 받게 됩니다.
네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **POST**  
- end point : **/nickname-check**

##### Request

###### Request Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| nickname | String | 중복확인 할 사용자의 닉네임 | O |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/nickname-check" \
 -d "nickname=뽀삐puppy12"
```
##### Response

###### Header

| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |

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

***

#### - 전화번호 인증 

##### 설명

클라이언트는 숫자로만 이루어진 11자리 전화번호를 입력하여 요청하고 이미 사용중인 전화번호인지 확인 후 4자리의 인증번호를 해당 전화번호에 문자를 전송합니다. 인증번호가 정상적으로 전송이 된다면 성공 응답을 받습니다. 만약 중복된 전화번호를 입력한다면 중복된 전화번호에 해당하는 응답을 받게됩니다. 네트워크 에러, 서버 에러, 데이터베이스 에러, 문자 전송 실패가 발생할 수 있습니다.  

- method : **POST**  
- URL : **/tel-auth**  

##### Request

###### Request Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| telNumber | String | 인증 번호를 전송할 사용자의 전화번호 (11자리 숫자) | O |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/tel-auth" \
 -d "telNumber=01011112222"
```

##### Response

###### Header

| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |

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

***

#### - 인증번호 확인  
  
##### 설명

클라이언트는 사용자 전화번호와 인증번호를 입력하여 요청하고 해당하는 전화번호와 인증번호가 서로 일치하는지 확인합니다. 일치한다면 성공에 대한 응답을 받습니다. 만약 일치하지 않는 다면 전화번호 인증 실패에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.  

- method : **POST**  
- end point : **/tel-auth-check**  

##### Request

###### Request Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| telNumber | String | 인증 번호를 확인할 사용자 전화번호 | O |
| authNumber | String | 인증 확인에 사용할 인증 번호 | O |

###### Example

```bash
curl -v -X POST "http://localhost:4000/api/v1/auth/tel-auth-check" \
 -d "telNumber=01011112222" \
 -d "authNumber=1234"
```

##### Response

###### Header

| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |

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

***

#### - 회원가입  
  
##### 설명

클라이언트는 사용자 이름, 사용자 아이디, 사용자 닉네임, 비밀번호, 전화번호, 인증번호, 가입경로, 프로필 이미지, 개인목표, 키, 몸무게, 골격근량, 체지방량, 벤치프레스, 데드리프트, 스쿼트, 개인목표를 입력하여 요청하고 회원가입이 성공적으로 이루어지면 성공에 대한 응답을 받습니다. 만약 존재하는 아이디, 닉네임 일 경우 중복된 아이디, 닉네임에 대한 응답을 받고, 
만약 존재하는 전화번호일 경우 중복된 전화번호에 대한 응답을 받고, 전화번호와 인증번호가 일치하지 않으면 전화번호 인증 실패에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.  

- method : **POST**  
- end point : **/sign-up**

##### Request

###### Request Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| profileImage | String | 사용자 프로필이미지 | X |
| name | String | 사용자의 이름 | O |
| userId | String | 사용자의 아이디 | O |
| nickname | String | 사용자의 닉네임 | O |
| password | String | 사용자의 비밀번호 (8~13자의 영문 + 숫자) | O |
| telNumber | String | 사용자의 전화번호 (11자의 숫자) | O |
| authNumber | String | 전화번호 인증번호 | O |
| joinPath | String | 회원가입 경로 (기본: 'HOME', 카카오: 'KAKAO', 네이버: 'NAVER') | O |
| snsId | String | SNS 가입시 sns oauth2 ID | X |
| height | String | 사용자 키 | O |
| weight | Float | 사용자 몸무게 | O |
| skeletalMuscleMass | Float | 골격근량  | X |
| bodyFatMass | Float | 사용자 체지방량 | X |
| deadlift | Float | 사용자 3대 측정 데드리프트 | X |
| benchPress | Float | 사용자 3대 측정 벤치프레스| X |
| squat | Float | 사용자 3대 측정 스쿼트 | X |
| personalGoal | String | 사용자 개인목표 | X |

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

| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |

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

***

#### - SNS 회원가입 및 로그인  
  
##### 설명

클라이언트는 OAuth 인증서버를 입력하여 요청하고 해당하는 Redirect 응답을 받습니다. 회원가입이 되어있는 사용자의 경우 쿼리 매개변수로 접근 토큰과 토큰 만료 기간을 반환하며 회원가입이 되어있지 않은 사용자의 경우 쿼리 매개변수로 sns 아이디와 해당하는 sns 서비스의 이름을 반환합니다.

- method : **GET**  
- end point : **/sns-sign-in/{registerId}** 

##### Request

###### Path Variable

| name | type | description | required |
|---|:---:|:---:|:---:|
| registerId | String | 사용 SNS (카카오: 'kakao', 네이버: 'naver') | O |

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
<h2 style='background-color: rgba(55, 55, 55, 0.2); text-align: center'>Customer 모듈</h2>

healthcare 서비스에 사용자와 관련된 REST API 모듈입니다.
사용자 정보확인, 정보수정, 3대측정 등록, 신체변화 등록 등의 API가 포함되어 있습니다.
User 모듈은 모두 인증이 필요합니다.

- url : /api/v1/customer

***

#### -  로그인 유저 정보 확인
  
##### 설명

클라이언트는 요청 헤더에 Bearer 인증 토큰을 포함하여 요청하고 성공적으로 이루어지면 성공에 대한 응답으로 토큰에 해당하는 사용자의 아이디와 이름, 닉네임을 응답 받습니다. 네트워크 에러, 서버 에러, 인증 실패, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**  
- URL : **/sign-in** 

##### Request

###### Header

| name | description | required |
|---|:---:|:---:|
| Authorization | Bearer 토큰 인증 헤더 | O |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/customer" 
```

| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |
| userId | String | 사용자 아이디 | O |
| name | String | 사용자 이름 | O |
| nickname | String | 사용자 닉네임 | O |

###### Example

**응답 성공**
```bash
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "code": "SU",
  "message": "Success.",
  "userId": "qwer1234",
  "name": "홍길동",
  "nickname": "뽀삐puppy12"
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

***

#### - 사용자 신체 정보 보기 
  
##### 설명

사용자는 요청 헤더에 Bearer 인증 토큰을 포함하고 URL에 사용자 아이디를 포함하여 요청하고 성공적으로 이루어지면 사용자 신체정보 번호, 사용자의 몸무게, 골격근량, 체지방량, 사용자 신체 정보 등록 날짜를 응답 받습니다. 만약 존재하지 않는 아이디일 경우 존재하지 않는 아이디에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.  

- method : **GET**  
- URL : **/{userId}**  

##### Request

###### Header

| name | description | required |
|---|:---:|:---:|
| Authorization | Bearer 토큰 인증 헤더 | O |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/customer/qwer1234" \
```

##### Response

###### Header
| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |
| user_muscle_fat_number | Integer | 사용자 신체 정보 번호 | O |
| userId | String | 사용자 아이디 | O |
| weight | Float | 몸무게 | O |
| skeletal_muscle_mass | Float | 골격근량 | X |
| body_fat_mass | Float | 체지방량 | X |
| user_muscle_fat_date | Date | 사용자 신체 정보 등록 날짜 | O |

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

***

#### - 사용자 3대 측정 정보
  
##### 설명

사용자는 요청 헤더에 Bearer 인증 토큰을 포함하고 URL에 사용자 아이디를 포함하여 요청하고 성공적으로 이루어지면 사용자 3대 측정 정보 번호, 데드리프트, 벤치프레스, 스쿼트, 사용자 3대 측정 정보 등록 날짜를 응답 받습니다. 만약 존재하지 않는 아이디일 경우 존재하지 않는 아이디에 대한 응답을 받습니다. 네트워크 에러, 서버 에러, 데이터베이스 에러가 발생할 수 있습니다.

- method : **GET**  
- URL : **/{userId}**  

##### Request

###### Header

| name | description | required |
|---|:---:|:---:|
| Authorization | Bearer 토큰 인증 헤더 | O |

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/customer/qwer1234"
```

##### Response

###### Header
| name | description | required |
|---|:---:|:---:|
| Content-Type | 반환되는 Response Body의 Content type (application/json) | O |

###### Response Body

| name | type | description | required |
|---|:---:|:---:|:---:|
| code | String | 결과 코드 | O |
| message | String | 결과 코드에 대한 설명 | O |
| health_machine_measurement_number | Integer | 사용자 3대 측정 정보 번호 | O |
| userId | String | 사용자 아이디 | O |
| deadlift | Float | 데드리프트(kg) | X |
| bench_press | Float | 벤치프레스(kg) | X |
| squat | Float | 스쿼트 | X |
| health_machine_measurement_date | Date | 사용자 3대 측정 정보 등록 날짜 | O |

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
***

<h2 style='background-color: rgba(55, 55, 55, 0.2); text-align: center'>Board 모듈</h2>

Healthcare 서비스의 게시물과 관련된 REST API 모듈입니다.  
게시물 리스트, 정보 보기, 게시물 등록, 게시물 수정, 게시물 삭제 등의 API가 포함되어 있습니다.
게시물 등록, 수정, 삭제는 인증이 필요합니다.
  
- url : /api/v1/board  

***

#### - 게시물 리스트 보기
  
##### 설명

모든 클라이언트는 게시물 번호, 게시물 제목, 게시물 작성자 닉네임, 게시물 작성날짜, 게시물 조회수가 조회가 되면 성공적으로 응답을 받습니다. 네트워크 에러, 서버에러, 데이터베이스 에러가 발생할 수 있습니다. 

- method : **GET**  
- URL : **/**  

##### Request

###### Example

```bash
curl -X GET "http://localhost:4000/api/v1/board" \