import json
from locust import HttpUser, task, between
import requests


class sample(HttpUser):
    def on_start(self):
        print("start test")

    def on_stop(self):
        print("end test")

    @task
    def login(self):
        X_AUTH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2OTQ2MjI3NDAsImV4cCI6MTY5NDYyNDU0MH0.toWyVYF48574DfOrVQeWRsuAFuJtXcgObnxfP58nqVw"

        url = "/services"
        headers = {
            "Content-Type": "application/json",
            "X-AUTH-TOKEN": X_AUTH_TOKEN,
        }
        data = {
            "category": "공공행정",
            "title": "날씨 데이터 제공 서비스",
            "description": "N일 전의 날씨 데이터를 제공하는 API 서비스",
            "price": 30000,
            "key": "12345",
            "apis": [
                {
                    "host": "localhost:8081",
                    "method": "GET",
                    "path": "/weather",
                    "description": "N일 전의 날씨 데이터를 제공하는 API",
                    "limitation": 12000,
                    "headers": [],
                    "requestParameters": '{"title":"루트","description":"JSON 최상위 객체","type":"object","properties":{"daysAgo":{"type":"number","description":"며칠 전의 날씨 데이터를 가져올 것인지 지정","title":"며칠 전의 날씨 데이터를 가져올 것인지 지정"}},"required":["daysAgo"]}',
                    "responseParameters": '{"title":"루트","description":"JSON 최상위 객체","type":"object","properties":{"date":{"type":"string","title":"날짜","description":"날짜"},"temperature":{"type":"number","title":"기온","description":"기온"},"humidity":{"type":"number","description":"습도","title":"습도"},"condition":{"type":"string","title":"환경","description":"환경"}},"required":["date","temperature","humidity","condition"]}',
                    "errorCodes": [
                        {"statusCode": 400, "description": "쿼리 파라미터의 값이 유효하지 않을 때 발생"},
                        {"statusCode": 401, "description": "API key가 유효하지 않을 때 발생"},
                        {"statusCode": 427, "description": "daysAgo의 값이 정수형이 아닐 때 발생"},
                        {"statusCode": 430, "description": "daysAgo의 값이 음수일 때 발생"},
                    ],
                }
            ],
        }

        self.client.post(url, json.dumps(data), headers=headers)
