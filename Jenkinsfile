pipeline {
    agent any

    parameters {
        string(
            name: 'BUILD_ID',
            description: 'Format yyyyMMddHHmm'
        )
        string(
            name: 'BRANCH_NAME',
            defaultValue: 'main',
            description: 'Git branch'
        )
    }

    environment {
        APP_NAME   = 'user-service'
        IMAGE_TAG = "${params.BUILD_ID}"
        IMAGE_NAME = "${APP_NAME}:${IMAGE_TAG}"
    }

    stages {

        stage('Validate Build ID') {
            steps {
                script {
                    if (!params.BUILD_ID.matches("\\d{12}")) {
                        error "BUILD_ID must be yyyyMMddHHmm"
                    }
                }
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: params.BRANCH_NAME,
                    url: 'https://github.com/ThinhND3004/temp_to_deploy.git'
            }
        }

        stage('Build JAR (Maven in Docker)') {
            steps {
                sh """
                docker run --rm \
                  -v "\$PWD":/app \
                  -v "\$HOME/.m2":/root/.m2 \
                  -w /app \
                  maven:3.9.6-eclipse-temurin-17 \
                  mvn clean package -DskipTests
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                docker build -t ${IMAGE_NAME} .
                """
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh """
                export IMAGE_TAG=${IMAGE_TAG}
                docker compose up -d ${APP_NAME}
                """
            }
        }
    }
}
