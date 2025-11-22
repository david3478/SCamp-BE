# S.CAMP : BE
2025 간지톤 10팀 — 스캠핑

- **서비스 개요** <br/>
  **S.CAMP**는 누구나 겪을 수 있는 피싱 사기를 예방하고 대처할 수 있도록 돕는 **AI 기반 보안 서비스**입니다.<br>
  주요 기능은 'AI 사기 탐지기', '사기 사례 게시판', '실시간 사기 뉴스'로 구성되어 있습니다.

  **AI 사기 탐지기**는 사용자가 입력한 URL을 분석하여 0부터 100까지 범위의 위험도 점수를 즉시 제공하며,<br>
  **실시간 사기 뉴스**는 네이버 Search API를 통해 최신 사기 뉴스 정보를 제공합니다.<br>
  또한 **사례 공유 게시판**을 통해 사용자 간의 예방 수칙 공유가 가능합니다.


- **기술 스택** <br/>
  <span>백엔드: </span>
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white">

  <span>인프라 및 DB: </span>
  <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white"> <img src="https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white">

- **팀원 소개**
  <table width="100%">
    <tr>
      <td align="center">
        <a href="https://github.com/david3478">
          <img src="https://github.com/david3478.png" width="100px" alt="김인우"/><br />
          <sub><b>김인우</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/rrinny">
          <img src="https://github.com/rrinny.png" width="100px" alt="박채린"/><br />
          <sub><b>박채린</b></sub>
        </a>
      </td>
    </tr>
    <tr>
      <td align="center">백엔드</td>
      <td align="center">백엔드</td>
    </tr>
    <tr>
      <td align="center">
        <p>Upstage API 연동</p>
        <p>AI URL 위험도 분석 로직</p>
      </td>
      <td align="center">
        <p>게시글 조회 API 구현</p>
        <p>Naver Search API 연동</p>
        <p>사기 뉴스 조회 로직</p>
      </td>
    </tr>
  </table>

- **폴더 구조**

    ```
    📂 scamp-backend
    └─ src
      ├─ main
      │  ├─ java
      │  │  └─ ganzi.scamp
      │  │     ├─ config      # Swagger, CORS 등 설정
      │  │     ├─ controller  # API 엔드포인트 (News, Post)
      │  │     ├─ dto         # 데이터 전송 객체
      │  │     ├─ entity      # DB 엔티티 정의 (Post)
      │  │     ├─ repository  # DB 접근 계층
      │  │     ├─ service     # 비즈니스 로직 (News, Post)
      │  │     └─ exception   # 전역 예외 처리 핸들러
      │  └─ resources
      │     ├─ application.yml # 서버 설정 파일
      │     └─ application-prod.properties
      └─ test                 # 테스트 코드
    ```

  - **개발 환경에서의 실행 방법**

    ```
    $ ./gradlew bootRun
    ```
