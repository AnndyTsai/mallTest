//运行mallTest脚本-接口自动化脚本
pipeline{ 
    agent {
       label "master"
       customWorkspace "/root/mallTest"
    }
    stages{      
        stage("获取mallTest接口自动化脚本源码"){           
            steps{               
                script{
                    sh "chmod 777 '${workspace}'"
					          echo '准备从github上克隆mallTest源码，由于clone比较慢，请等待'
					          //从github上拉取代码
					          git credentialsId: '84631f16-4000-4591-b342-068979628e', url: 'https://github.com/AnndyTsai/mallTest.git'
                }
            }
        }     
            
		stage("参数化构建mallTest"){
			steps{
			    script{
					echo '开始执行自动化测试'
					//进入工作目录
					sh "cd ${workspace}; \
						  mvn clean test"
					echo "构建成功...."
				}
			}
		}    
    }
}
