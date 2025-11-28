pipeline {
    agent any
    environment {
    
        DOCKERHUB_CRED = credentials('dockerhub')
    }
    stages {
        stage('Build Docker Image') {
            steps {
                dir('/mnt/c/Users/yahya/alpine-project') {
                    sh 'docker build -t $DOCKERHUB_CRED_USR/alpine:1.0.0 .'
                }
            }
        }
        stage('Push to DockerHub') {
            steps {
                sh 'echo "$DOCKERHUB_CRED_PSW" | docker login -u "$DOCKERHUB_CRED_USR" --password-stdin'
                sh 'docker push $DOCKERHUB_CRED_USR/alpine:1.0.0'
            }
        }
    }
}
