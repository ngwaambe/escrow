#!groovy

node {
    checkout scm
}

pipeline {
    agent any

    tools {
        jdk 'openjdk-11'
        maven '3.8.1'
    }

    stages {
        stage('Checkout') {
           steps {
              git 'https://github.com/ngwaambe/escrow.git'
           }
        }
        stage("Build and package") {
            steps {
                sh 'mvn clean verify'
            }
        }
    }
}
