pipeline
{
    agent
    {
        //아래의 작업이 수행될 에이전트
        label 'jenkins_agent_body'
    }

    environment
    {
        //Environment file
        ENV_FILE = '.env'
        //Discord Webhook URL
        WEBHOOK_URL = credentials('DISCORD_BODY_BUDDY')
        //Branch name
        BRANCH_NAME = 'develop'
        //Docker image name
        DOCKER_IMAGE = 'boby_buddy_be'
        //Docker container name
        DOCKER_CONTAINER = 'body_buddy_be'
        //Internal IP address of the container
        CONTAINER_IP = '172.20.0.113'
        //Spring Boot Profile
        SPRING_PROFILES_ACTIVE = 'prod'
    }

    tools
    {
        //Gradle 8.11.1
        gradle 'body_buddy_gradle'
    }

    options 
    {
        //Enable timestamps
        timestamps()
    }

    stages
    {
        stage('Checkout Backend Repository')
        {
            steps
            {
                echo 'Checking out Backend Repository (develop branch)'
                //이전에 체크아웃된 프론트엔드 레포지토리 삭제
                deleteDir()
                //프론트엔드 레포지토리를 develop 브랜치로 체크아웃
                git branch: "${BRANCH_NAME}", url: 'https://github.com/Team-BodyBuddy/bodybuddyV2-BE'
            }
        }

        stage('Create .env File')
        {
            steps
            {
                script
                {
                    run_with_error_handle('Create .env File')
                    {
                        echo 'Creating .env File'
                        //백엔드 레포지토리의 .env 파일 생성
                        sh '''
                            set +x
                            echo "MYSQL_URL=${MYSQL_URL}" > .env
                            echo "MYSQL_PORT=${MYSQL_PORT}" >> .env
                            echo "MYSQL_USER=${MYSQL_USER}" >> .env
                            echo "MYSQL_PW=${MYSQL_PW}" >> .env
                            echo "auth.secret-key=${JWT_SECRET}" >> .env
                            echo "SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}" >> .env
                            chmod 600 .env
                        '''
                    }
                }
            }
        }

        //stage('Gradle Test')
        //{
        //    steps
        //    {
        //        script
        //        {
        //            run_with_error_handle('Gradle Test')
        //            {
        //                echo 'Building Backend'
        //                //백엔드 레포지토리의 테스트
        //                sh 'gradle test'
        //            }
        //        }
        //    }
        //}

        stage('Gradle Clean Build')
        {
            steps
            {
                script
                {
                    run_with_error_handle('Gradle Clean Buiild')
                    {
                        echo 'Building Backend'
                        //백엔드 레포지토리의 빌드
                        sh 'SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE} gradle clean build'
                    }
                }
            }
        }

        stage('Build Docker Image')
        {
            steps
            {
                script
                {
                    run_with_error_handle('Build Docker Image')
                    {
                        echo 'Building Docker Image & Tagging'
                        //도커 이미지 빌드 및 latest 태그
                        sh '''
                            docker build --cache-from ${DOCKER_IMAGE}:latest \
                                --build-arg BUILD_BRANCH="${BRANCH_NAME}" \
                                --build-arg BUILD_NUMBER="${BUILD_NUMBER}" \
                                --build-arg BUILD_DATE="${BUILD_TIMESTAMP}" \
                                -t ${DOCKER_IMAGE}:dev-${BUILD_NUMBER} .
                            docker tag ${DOCKER_IMAGE}:dev-${BUILD_NUMBER} ${DOCKER_IMAGE}:latest
                        '''
                    }
                }
            }
        }

        stage('Deploy Docker Image')
        {
            steps
            {
                script
                {
                    run_with_error_handle('Deploy Docker Image')
                    {
                        echo 'Deploying Docker Image'
                        //기존 도커 컨테이너 삭제
                        //도커 이미지를 컨테이너로 실행
                        //Watchtower가 동작하지 않도록 아래의 옵션을 추가
                        //--label 'com.centurylinklabs.watchtower.enable=false'
                        sh '''
                            docker stop "${DOCKER_CONTAINER}" || true
                            docker rm "${DOCKER_CONTAINER}" || true
                            docker run -d \
                                --name "${DOCKER_CONTAINER}" \
                                --network jenkins_default \
                                --ip "${CONTAINER_IP}" \
                                -p 8080:8080 \
                                --label "com.centurylinklabs.watchtower.enable=false" \
                                --env-file ${ENV_FILE} \
                                --restart unless-stopped \
                                "${DOCKER_IMAGE}:latest"
                            rm -f ${ENV_FILE}
                        '''
                    }
                }
            }
        }
    }

    post
    {
        success
        {
            //빌드 성공 디스코드 메시지
            discordSend description: "Build Success! Your backend is ready.", 
                            footer: "The backend build has successfully completed without any errors.", 
                            link: env.BUILD_URL, result: currentBuild.currentResult, 
                            title: "${env.JOB_NAME} #${BUILD_NUMBER}", 
                            webhookURL: env.WEBHOOK_URL
        }

        failure
        {
            //빌드 실패 디스코드 메시지
            discordSend description: "Build Failed! Check the output for more details.", 
                            //footer: "The build process encountered errors.", 
                            link: env.BUILD_URL, result: currentBuild.currentResult, 
                            title: "${env.JOB_NAME} #${BUILD_NUMBER}", 
                            webhookURL: env.WEBHOOK_URL
        }
    }
}

def run_with_error_handle(String stage_name, Closure task)
{
    try
    {
        //작업 수행
        task.call()
    }
    catch (Exception error)
    {
        //빌드 실패 디스코드 메시지
        currentBuild.result = "FAILURE"
        discordSend description: "Build Failed: ${stage_name}", 
                            footer: "The build process encountered errors.\nPlease review the log for detailed information on what went wrong and how to fix it.\n\n${error.message}", 
                            link: env.BUILD_URL, result: currentBuild.currentResult, 
                            title: "${env.JOB_NAME} #${BUILD_NUMBER}", 
                            webhookURL: env.WEBHOOK_URL
        //스테이지 실패
        error("Stage failed: ${stage_name}")
    }
}