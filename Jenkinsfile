pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    parameters {
        string(name: 'BUILD_ID', defaultValue: '', description: 'YYYYMMDDHHmm')
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Git branch')
    }

    environment {
        APP_NAME = 'user-service'
        IMAGE_TAG = "${params.BUILD_ID}"
        IMAGE_NAME = "${APP_NAME}:${IMAGE_TAG}"
        GIT_URL = 'https://github.com/ThinhND3004/temp_to_deploy.git'
    }

    stages {

        stage('Validate Parameters') {
            steps {
                script {
                    if (!params.BUILD_ID.matches("\\d{12}")) {
                        error "BUILD_ID must be YYYYMMDDHHmm"
                    }
                }
            }
        }

        stage('Checkout Source') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH_NAME}"]],
                    userRemoteConfigs: [[url: "${GIT_URL}"]]
                ])
            }
        }

        stage('Build JAR (Maven)') {
            steps {
                sh '''
                docker run --rm \
                  -v "$PWD":/app \
                  -v "$HOME/.m2":/root/.m2 \
                  -w /app \
                  maven:3.9.6-eclipse-temurin-17 \
                  mvn clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                export IMAGE_TAG=$IMAGE_TAG
                docker compose down
                docker compose up -d
                '''
            }
        }
    }
}
