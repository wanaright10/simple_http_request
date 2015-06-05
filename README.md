# simple_http_request
最简单的HttpRequest请求

实例：
GET


        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("key", "value");
        queryParam.put("chinese", "中文");

        HttpResponse response = HttpRequest.create()
                .get("http://domain:port/some/path", queryParam, true)//也可以不传 也可以不加密
                .execute();

        int responseCode = response.getCode();
        String responseStr = response.getBody();
        
POST


        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("key", "value");
        queryParam.put("chinese", "中文");

        HttpResponse response = HttpRequest.create()
                .post("http://domain:port/some/path", queryParam, true)//也可以不传 也可以不加密
                .execute();

        int responseCode = response.getCode();
        String responseStr = response.getBody();
