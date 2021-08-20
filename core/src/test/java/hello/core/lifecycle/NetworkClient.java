package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient {

    private String url;

    public NetworkClient(){
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        System.out.println("NetworkClient.setUrl");
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect(){
        System.out.println("connect = " + url);
    }

    public void call(String message){
        System.out.println("call : "+ url + ", message = " + message);
    }

    //서비스 종료시 호출
    public void disconnect(){
        System.out.println("close = " + url);
    }

    public void init() { //의존관계셋팅 후 실행
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메세지");
    }

    public void close() {    //종료시 호출
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}
