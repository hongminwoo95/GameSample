package Main;

import java.awt.Dimension; // 
import java.awt.Graphics;
import java.awt.Image; // 이미지불러오는 메소드
import java.awt.Toolkit; // 이미지파일 경로 찾아올수 있는것
import java.awt.event.KeyEvent; // 키 이벤트 가능하게 해주는 임포트
import java.awt.event.KeyListener; // 키이벤트 수신하도록 등록된 List 목록?? 같은느낌? 셋트인듯?
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO; //ImageReaders와 ImageWriters를 찾고 간단한 인코딩과 디코딩을 수행하기 위한 정적 편의 메서드를 포함하는 클래스입니다 .
import javax.swing.JFrame;// Frame클래스 속성중에서 사용자에게 보여줄것인지 아닌지 boolean타입
//.setVisible(true) : 창을 화면에 나타낼 것인지 설정 여부
//.setSize(300,300) : 창의 가로와 세로 길이를 설정
// 등등많음 검색으로 찾고 정리하기

// <멀티 스레드>
// 모든 자바 어플리케이션은 Main Thread가 main() 메소드를 실행하면서 시작됩니다. 예외는없음!!
// 이러한 Main Thread 흐름 안에서 싱글 스레드가 아닌 멀티 스레드 어플리케이션은 필요에 따라 작업 쓰레드를 만들어 병렬로
// 코드를 실행할 수 있습니다. 싱글 스레드 같은 경우 메인 스레드가 종료되면 프로세스도 종료되지만,
// 멀티 스레드는 메인 스레드가 종료되더라도 실행 중인 스레드가 하나라도 있다면 프로세스는 종료되지 않습니다.

public class game {
	
	public static void main(String[] args) {
		game_Frame fms = new game_Frame();
	}
}
// <멀티 스레드>
// 모든 자바 어플리케이션은 Main Thread가 main() 메소드를 실행하면서 시작됩니다. 예외는없음!!
// 이러한 Main Thread 흐름 안에서 싱글 스레드가 아닌 멀티 스레드 어플리케이션은 필요에 따라 작업 쓰레드를 만들어 병렬로
// 코드를 실행할 수 있습니다. 싱글 스레드 같은 경우 메인 스레드가 종료되면 프로세스도 종료되지만,
// 멀티 스레드는 메인 스레드가 종료되더라도 실행 중인 스레드가 하나라도 있다면 프로세스는 종료되지 않습니다.

//	                        ↓ 프레임생성을 위한상속   ↓키이벤트 가능하게 해주는 임포트 
class game_Frame extends JFrame implements KeyListener, Runnable {
	// 프레임 생성을 위한 JFrame 상속
	// 키보드 이벤트 처리를 위한 KeyListener를 상속
	// 스레드를 돌리기 위한 Runnable 상속
	
	int f_width = 800;
	int f_height = 600;
	
	int x, y; // 캐릭터의 좌표 변수

	int[] cx ={0, 0, 0}; // 배경 스크롤 속도 제어용 변수
	int bx = 0; // 전체 배경 스크롤 용 변수
	
	boolean KeyUp = false; // 키보드 방향키 입력 처리를 위한 변수
	boolean KeyDown = false; // 키보드 방향키 입력 처리를 위한 변수
	boolean KeyLeft = false; // 키보드 방향키 입력 처리를 위한 변수
	boolean KeyRight = false; // 키보드 방향키 입력 처리를 위한 변수
	boolean KeySpace = false; //미사일 발사를 위한 키보드 스페이스키 입력을 추가합니다.
	
	int cnt; //각종 타이밍 조절을 위해 무한 루프를 카운터할 변수
	
	int player_Speed; // 유저의 캐릭터가 움직이는 속도를 조절할 변수
	int missile_Speed; // 미사일이 날라가는 속도 조절할 변수
	int fire_Speed; // 미사일 연사 속도 조절 변수
	int enemy_speed; // 적 이동 속도 설정
	int player_Status = 0; 
	// 유저 캐릭터 상태 체크 변수 0 : 평상시, 1: 미사일발사, 2: 충돌
	int game_Score; // 게임 점수 계산
	int player_Hitpoint; // 플레이어 캐릭터의 체력

	//int e_w, e_h;  //소스 변경으로 인해 해당 변수가 필요없어졌습니다
	//int m_w, m_h; 
	
	int e_w, e_h; //적 이미지의 크기값을 받을 변수
	int m_w, m_h; //미사일 이미지의 크기값을 받을 변수
	
	Thread th; // 스레드 생성
	Image Missile_img; //미사일 이미지 변수
	Image Enemy_img; // 적 이미지를 받아들일 이미지 변수
	
	ArrayList Missile_List = new ArrayList(); //다수의 미사일을 관리하기 위한 배열
	ArrayList Enemy_List = new ArrayList(); //다수의 적을 등장 시켜야 하므로 배열을 이용.
	
	
	Toolkit tk = Toolkit.getDefaultToolkit(); // Toolkit.getDefaultToolkit <- GUI 작업시 사용하는 유틸리티 클래스
	// Toolkit tk = Toolkit.getDefaultToolkit() + .getImage(”경로파일”) <--적으면 파일위치
	// 찾아갈수있음
	// 이미지를 불러오기 위한 툴킷
	
	
	Image me_img;
	// 해당 저장폴더 위치에 f15k.png 라는 이미지를 불러옵니다.
	
	
	Missile ms; // 미사일 클래스 접근 키
	Enemy en; //에너미 클래스 접근 키
	
	
	Image buffImage; //더블 버퍼링용
	Graphics buffg; //더블 버퍼링용
	
	
	game_Frame() {
		init();
		start();
		
		setTitle("슈팅 게임 만들기"); // GUI 타이틀
		setSize(f_width, f_height); // GUI 사이즈

		// ↓ 윈도우창 조절 ↓이미지
		Dimension screen = tk.getScreenSize(); //스크린사이즈의 이미지를 불러온다
		// 모니터 해상도를 얻어오는 명령
		
		int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2); // 모니터 해상도 관계없이 프로그램이 시작되면 윈도우 화면 정가운데 표시
		int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2); // ↑ 동일하게보기 세트
		
		setLocation(f_xpos, f_ypos); // 윈도우창 위치설정 ↑ 위에 정가운데 좌표입력했으니 정 중앙에 표현
		setResizable(false); // 창 크기조절 가능하게 할지 여부 (마우스로 늘리고 줄이고 행위)
		setVisible(true); // 임포트JFrame boolean 타입 true로 창열수있게끔 해주는 코드
	}
	
	public void init() { // 캐릭터 최초 좌표
		
		x = 100;
		y = 100;
		f_width = 1200;
		f_height = 1000;
		
		me_img = tk.getImage("C:/Users/hu-11/Desktop\\이미지/ccc.png");
			//   ↑ tk = <- Toolkit.getDefaultToolkit GUI 작업시 사용하는 유틸리티 클래스를 참조하여 이미지를 보낸다
		Missile_img = tk.getImage("C:/Users/hu-11/Desktop\\이미지/qqq.jpg");
			//   ↑ tk = <- Toolkit.getDefaultToolkit GUI 작업시 사용하는 유틸리티 클래스를 참조하여 이미지를 보낸다
		Enemy_img = tk.getImage("C:/Users/hu-11/Desktop\\이미지/qqq.jpg");  //적 이미지 생성
		
		e_w = ImageWidthValue("C:/Users/hu-11/Desktop\\\\이미지/aaa.png");
		e_h = ImageHeightValue("C:/Users/hu-11/Desktop\\\\이미지/aaa.png");
		//적 이미지의 w(넓이)값, h(높이) 값을 계산하여 받습니다.
		//해당 메소드는 아래에 이미지 크기값 계산용으로
		//추가된 메소드 입니다.
		

		m_w = ImageWidthValue("C:/Users/hu-11/Desktop\\\\이미지/qqq.jpg");
		m_h = ImageHeightValue("C:/Users/hu-11/Desktop\\\\이미지/qqq.jpg");
		//미사일 이미지의 w(넓이)값, h(높이) 값
	}
	
	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 오른쪽 위에 X버튼을 눌렀을때 프로그램 실행 중지
		addKeyListener(this); // 키보드 이벤트 실행
		th = new Thread(this); // 스레드 생성
		th.start(); // 스레드 실행
	}
	
	public void run() { // 스레드가 무한 루프될 부분 (스레드 내부에 따로있음)
		try { // 예외옵션 설정으로 에러 방지
			while (true) {         // while 문으로 무한 루프 시키기
				KeyProcess();      // 키보드 입력처리를 하여 x,y 갱신
				EnemyProcess();    //적 움직임 처리 메소드 실행
				MissileProcess();  // 미사일 처리 메소드 실행
				repaint(); // 갱신된 x,y값으로 이미지 새로 그리기
				Thread.sleep(20); // 20 milli sec 로 스레드 돌리기
				cnt ++;           //무한 루프 카운터
			}
		} catch (Exception e) {
		}
	}
	public void MissileProcess(){ // 미사일 처리 메소드
		if ( KeySpace ){ 		  // 스페이스바 키 상태가 true 면
		ms = new Missile(x+300,y+30);    // 좌표 체크하여 넘기기 안쪽 인수는 미사일 정확한 위치를 표시하기위함
		Missile_List.add(ms);      // 해당 미사일 추가
		}

		for ( int i = 0 ; i < Missile_List.size() ; ++i){
			ms = (Missile) Missile_List.get(i);
			ms.move();
			if ( ms.x > f_width - 20 ){
			Missile_List.remove(i);
			}
			//편의상 그림그리기 부분에 있던 미사일 이동과
			//미사일이 화면에서 벗어났을시 명령 처리를
			//이쪽으로 옮겼습니다.
		for (int j = 0 ; j < Enemy_List.size(); ++ j){
			en = (Enemy) Enemy_List.get(j);
			if (Crash(ms.x,ms.y,en.x,en.y, m_w, m_h, e_w, e_h)){
			//미사일과 적 객체를 하나하나 판별하여
			//접촉했을시 미사일과 적을 화면에서 지웁니다.
			//판별엔 Crash 메소드에서 계산하는 방식을 씁니다.
			Missile_List.remove(i);
			Enemy_List.remove(j);
			}
		}
		}
		
	}
	public void EnemyProcess(){//적 행동 처리 메소드

		for (int i = 0 ; i < Enemy_List.size() ; ++i ){ 
		en = (Enemy)(Enemy_List.get(i)); 
		//배열에 적이 생성되어있을 때 해당되는 적을 판별
		en.move(); //해당 적을 이동시킨다.
			if(en.x < -200){ //적의 좌표가 화면 밖으로 넘어가면
			Enemy_List.remove(i); // 해당 적을 배열에서 삭제
			}
		}
		if ( cnt % 300 == 0 ){ //루프 카운트 300회 마다
		en = new Enemy(f_width + 100, 100);
		Enemy_List.add(en); 
		//﻿각 좌표로 적을 생성한 후 배열에 추가한다.
		en = new Enemy(f_width + 100, 200);
		Enemy_List.add(en);
		en = new Enemy(f_width + 100, 300);
		Enemy_List.add(en);
		en = new Enemy(f_width + 100, 400);
		Enemy_List.add(en);
		en = new Enemy(f_width + 100, 500);
		Enemy_List.add(en);
		}

	}
	public boolean Crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2){
		//﻿충돌 판정을 위한 새로운 Crash 메소드를 만들었습니다.
		//판정을 위해 충돌할 두 사각 이미지의 좌표 및 
		//넓이와 높이값을 받아들입니다.
		//여기서 이미지의 넓이, 높이값을 계산하기 위해 밑에 보면
		//이미지 크기 계산용 메소드를 또 추가했습니다.
		boolean check = false;

		if ( Math.abs( ( x1 + w1 / 2 )  - ( x2 + w2 / 2 ))  <  ( w2 / 2 + w1 / 2 )  
		&& Math.abs( ( y1 + h1 / 2 )  - ( y2 + h2 / 2 ))  <  ( h2 / 2 + h1/ 2 ) ){
		//충돌 계산식입니다. 사각형 두개의 거리및 겹치는 여부를 확인
		//하는 방식이라고 알고 있는데, 만들다보니 생각보다 식이 
		//복잡해진것 같습니다.
		//이보다 더 간편한 방식이 있을 것도 같은데 
		//일단 이렇게 해봤습니다.
		check = true;//위 값이 true면 check에 true를 전달합니다.
		}else{ check = false;}

		return check; //check의 값을 메소드에 리턴 시킵니다.

	}
	public void paint(Graphics g){ //
		buffImage = createImage(f_width, f_height); 
		//               ↑ createImage <- 지정된 바이트 배열에 저장된 이미지를 지정된 오프셋과 길이로 디코딩하는 이미지 생성 (버퍼)
		//더블버퍼링 버퍼 크기를 화면 크기와 같게 설정
		buffg = buffImage.getGraphics(); // 버퍼의 그래픽 객체를 얻기
		update(g);
	}
	
	public void Draw_Char(){ // 실제로 그림들을 그릴 부분
		buffg.clearRect(0, 0, f_width, f_height);
		buffg.drawImage(me_img, x, y, this);
	}
	public void Draw_Missile(){ // 미사일 그리는 메소드
		for (int i = 0 ; i < Missile_List.size()  ; ++i){
		//미사일 존재 유무를 확인한다.

		ms = (Missile) (Missile_List.get(i)); 
		// 미사일 위치값을 확인

		buffg.drawImage(Missile_img, ms.x + 150, ms.y + 30, this); 
		// 현재 좌표에 미사일 그리기.
		// 이미지 크기를 감안한 미사일 발사 좌표는 수정됨.

		ms.move();
		// 그려진 미사일을 정해진 숫자만큼 이동시키기

		if ( ms.x > f_width ){ // 미사일이 화면 밖으로 나가면
		Missile_List.remove(i); // 미사일 지우기
		}
		}
	}
	public void update(Graphics g){
		Draw_Char();// 실제로 그려진 그림을 가져온다
		
		Draw_Enemy(); // 그려진 적 이미지를 가져온다.
		Draw_Missile(); // 그려진 미사일 가져와 실행
		g.drawImage(buffImage, 0, 0, this); 
		// 화면에 버퍼에 그린 그림을 가져와 그리기 버퍼에 덮어씌우기
	}
	
	public void keyPressed(KeyEvent e) {
		// 키보드가 눌러졌을때 이벤트 처리하는 곳
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = true;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = true;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = true;
			break;
		case KeyEvent.VK_SPACE : // 스페이스키 입력 처리 추가
			KeySpace = true;
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		// 키보드가 눌러졌다가 때어졌을때 이벤트 처리하는 곳
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = false;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = false;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = false;
			break;
		case KeyEvent.VK_SPACE : // 스페이스키 입력 처리 추가
			KeySpace = false;
			break;
		}
	}

	public void keyTyped(KeyEvent e) {
	}
	
	
	// 키보드가 타이핑 될때 이벤트 처리하는 곳
	public void KeyProcess() { // <--- 물체이동 속도를 조절할수있음
		// 실제로 캐릭터 움직임 실현을 위해
		// 위에서 받아들인 키값을 바탕으로
		// 키 입력시마다 5만큼의 이동을 시킨다.

		if (KeyUp == true)
			y -= 5;
		if (KeyDown == true)
			y += 5;
		if (KeyLeft == true)
			x -= 5;
		if (KeyRight == true)
			x += 5;
	}
	public int ImageWidthValue(String file){ 
		// 이미지 넓이 크기 값 계산용 메소드 입니다.
		// 파일을 받아들여 그 파일 값을 계산 하도록 하는 것입니다.
		int x = 0;
		try{
		File f = new File(file); // 파일을 받습니다.
		BufferedImage bi = ImageIO.read(f);
		//받을 파일을 이미지로 읽어들입니다.
		x = bi.getWidth(); //이미지의 넓이 값을 받습니다.
		}catch(Exception e){}
		return x; //받은 넓이 값을 리턴 시킵니다.
		}

		public int ImageHeightValue(String file){ // 이미지 높이 크기 값 계산
		int y = 0;
		try{
		File f = new File(file);
		BufferedImage bi = ImageIO.read(f);
		y = bi.getHeight();
		}catch(Exception e){}
		return y;
		}
	class Missile{ // 미사일 위치 파악 및 이동을 위한 클래스 추가 

		int x;
		int y; //편의상 변수 명 변경
		Missile(int x, int y){ //미사일 좌표를 입력 받는 메소드
			this.x = x; 
			this.y = y;//편의상 변수명 변경
		}
		
		public void move(){ //미사일 이동을 위한 메소드
		x += 10; //x 좌표에 10만큼 미사일 이동
		}
		
	}
	public void Draw_Enemy(){ // 적 이미지를 그리는 부분
		for (int i = 0 ; i < Enemy_List.size() ; ++i ){
		en = (Enemy)(Enemy_List.get(i));
		buffg.drawImage(Enemy_img, en.x, en.y, this);
		
		//배열에 생성된 각 적을 판별하여 이미지 그리기
		}
	}
	
	class Enemy{ // 적 위치 파악 및 이동을 위한 클래스
		int x;
		int y;

		Enemy(int x, int y){ // 적좌표를 받아 객체화 시키기 위한 메소드
		this.x = x;
		this.y = y;
		}
		public void move(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
		x -= 3;
		}
	}
	
}
