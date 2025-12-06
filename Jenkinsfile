pipeline {
    agent any
     tools {
        maven "M2_HOME"
    }
    
    environment {
    
        DOCKERHUB_CRED = credentials('dockerhub')
    }
    stages {
        stage("Code Checkout") {
            steps {
                git branch: 'main',
                    url: 'https://github.com/yahyaelkaed/yahyaelkaed-SAE9-jenkins'
            }
        }
        stage('Code Test') {
            steps {
                sh "mvn test"
            }
        }
        stage('Code Build') {
            steps {
                sh "mvn package"
            }
        }
 stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.login=$SONAR_TOKEN \
                            -Dsonar.java.coveragePlugin=jacoco \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
            
            post {
                success {
                    echo "✅ SonarQube analysis submitted successfully"
                    script {
                        // Get the actual SonarQube URL from environment
                        def sonarUrl = env.SONAR_HOST_URL ?: 'http://localhost:9000'
                        echo "Check results at: ${sonarUrl}/dashboard?id=tn.esprit%3Astudent-management"
                    }
                }
                failure {
                    echo "❌ SonarQube analysis failed"
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t $DOCKERHUB_CRED_USR/student:latest ."
                }
            }
        }
        
        stage('Push to DockerHub') {
            steps {
                sh 'echo "$DOCKERHUB_CRED_PSW" | docker login -u "$DOCKERHUB_CRED_USR" --password-stdin'
                sh 'docker push $DOCKERHUB_CRED_USR/student:latest'
            }
        }
        stage("Deploy to Kubernetes") {
    steps {
        withCredentials([
            string(credentialsId: 'K8S_TOKEN', variable: 'TOKEN')
            // CA_FILE removed because we skip TLS verification
        ]) {
            sh """
                # Configure kubectl for Jenkins using service account token
                kubectl config set-cluster minikube \
                    --server=https://localhost:32771 \
                    --insecure-skip-tls-verify=true

                kubectl config set-credentials jenkins \
                    --token=$TOKEN

                kubectl config set-context jenkins-context \
                    --cluster=minikube \
                    --user=jenkins

                kubectl config use-context jenkins-context

                # Apply Kubernetes manifests
                kubectl apply -f k8s/mysql/mysql-deployment.yaml -n devops
                kubectl apply -f k8s/mysql/mysql-pv-pvc.yaml -n devops
                kubectl apply -f k8s/mysql/mysql-service.yaml -n devops
                kubectl apply -f k8s/spring/spring-deployment.yaml -n devops
                kubectl apply -f k8s/spring/spring-service.yaml -n devops
            """
        }
    }
}



}

}
