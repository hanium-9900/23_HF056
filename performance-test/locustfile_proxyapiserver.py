from locust import HttpUser, task


class sample(HttpUser):
    def on_start(self):
        print("start test")

    def on_stop(self):
        print("end test")

    @task
    def getProxyApiTest(self):
        serviceId = 1
        apiPath = "weather"
        purchaseApiKey = "59510d91a04a1af467946ec474bb7593d71a8af45f3dc1911dd5b3cbf62478a2"
        daysAgo = 3

        self.client.get(f'/services/{serviceId}/{apiPath}?key={purchaseApiKey}&daysAgo={daysAgo}')
