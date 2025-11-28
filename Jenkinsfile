pipeline {
    agent any
    environment {
        DOCKERHUB_USER = credentials('dockerhub-username-id')
        DOCKERHUB_PASS = credentials('dockerhub-token-id')
    }
    stages {
        stage('Build Docker Image') {
            steps {
                dir('/mnt/c/Users/yahya/alpine-project') {
                    sh 'docker build -t $DOCKERHUB_USER/alpine:1.0.0 .'
                }
            }
        }
        stage('Push to DockerHub') {
            steps {
                sh 'echo "$DOCKERHUB_PASS" | docker login -u "$DOCKERHUB_USER" --password-stdin'
                sh 'docker push $DOCKERHUB_USER/alpine:1.0.0'
            }
        }
    }
}
