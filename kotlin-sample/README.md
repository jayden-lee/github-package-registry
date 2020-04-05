# Kotlin Example

## Github 토큰 생성
토큰을 생성하기 위해서는 아래 경로로 이동한다.
 
- Settings > Developer Settings > Personal Access Token
- 패키지를 배포 및 읽기 권한을 선택한다.
    - write:packages
    - read:packages

## Gradle 빌드 파일 수정
Maven 패키지를 업로드 하기 위해서 <code>build.gradle.kts</code> 파일에 플러그인 추가와 설정 정보를 추가한다.

<code>GPR_USER</code>는 Github 계정 이름이며, <code>GPR_API_KEY</code>는 액세스 토큰 값이다.

```
plugins {
    // ...
    id("maven-publish")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jayden-lee/github-package-registry")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
            }
        }
    }

    publications {
        create<MavenPublication>("default") {
            from(components["java"])
        }
    }
}
```

## 패키지 게시
```
./gradlew publish
```

## Github Action 설정
```
name: Publish

on:
  push:
    branches:
    - master
    - release/*

jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v1
      - name: Set up JDK 1.8
        uses: actions/setup-java@v1
        with:
          java-version: 1.8
      - name: Grant execute permission for gradlew
        run: chmod +x kotlin-sample/gradlew
      - name: Publish to Github registry
        env:
          GPR_USER: ${{ secrets.GPR_USER }}
          GPR_API_KEY: ${{ secrets.GPR_API_KEY }}
        run: cd kotlin-sample && ./gradlew publish
```

## Github 패키지 종속성 추가하기
다른 코틀린 프로젝트에서 Github에 있는 패키지를 종속성으로 추가하려면, <code>build.gradle.kts</code> 설정 파일에 아래 정보를 추가해야 한다.

```
plugins {
    // ...
    id("maven")
}

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/jayden-lee/github-package-registry")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
        }
    }
}

dependencies {
    implementation("com.jayden.common:kotlin-sample:1.0.6")
}
``` 