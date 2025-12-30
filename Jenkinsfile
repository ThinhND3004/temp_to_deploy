pipeline {
    agent any

    parameters {
        string(
            name: 'BUILD_ID',
            description: 'Build ID format: YYYYMMDDHHmm (e.g. 202512252118)'
        )
        string(
            name: 'BRANCH_NAME',
            defaultValue: 'main',
            description: 'Git branch'
        )
    }

    environment {
        APP_NAME = 'user-service'
        IMAGE_TAG = "${BUILD_ID}"
        IMAGE_NAME = "user-service:${IMAGE_TAG}"
    }

    stages {

        stage('Validate Build ID') {
            steps {
                script {
                    if (!params.BUILD_ID.matches("\\d{12}")) {
                        error "BUILD_ID must be in format YYYYMMDDHHmm"
                    }
                }
            }
        }

//         stage('Checkout Code') {
//             steps {
//                 git branch: "${BRANCH_NAME}",
//                     url: 'https://github.com/ThinhND3004/temp_to_deploy.git'
//             }
//         }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
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
                docker compose down
                docker compose up -d
                """
            }
        }
    }
}
