# 데이터 유통 API 플랫폼

> 2023 한이음 ICT 자율형

## TEAM

| 역할 | 이름   |
| ---- | ------ |
| 팀장 | 박성준 |
| 팀원 | 박민제 |
| 팀원 | 변찬혁 |
| 팀원 | 송민하 |
| 팀원 | 이승헌 |

## Connect to AWS EC2
`ssh -i "ssh_key.pem" ubuntu@ec2-3-34-215-14.ap-northeast-2.compute.amazonaws.com`

## How to use locust base load test
1. Check your python version is 2.7 or later than 3.3
2. If you haven't installed locust, write script below  
`pip install locust` or `pip3 install locust`(if your python version is over 3.3)
3. After locust installed, you can check you locust version as script below  
`python -m locust -V`
4. Before test, you should **check your target spring server and linked database server is on your local environment** to avoid heavy AWS fee.
5. To run load test, select one of scrips below.  
5-1. Test for api-platform server: `python -m locust -f locustfile_apiplatform.py`  
5-2. Test for api-platform server: `python -m locust -f locustfile_proxyapiserver.py`  
6. If you successfully wrote your locust python script, you can see messages like capture below  
>`[date time] {user}/INFO/locust.main: Starting web interface at http://0.0.0.0:8089 (accepting connections from all network interfaces)`  
`[date time] {user}/INFO/locust.main: Starting Locust 2.16.1`    
for more information, you can check https://locust.io/
