package castle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import feature.BackGroundItem;
import feature.Exit;
import feature.Feature;
import feature.Item;
import feature.Person;
import map.Room;

public class Game {
    private Room currentRoom;
    private Feature currentFeature;
    private Room targetRoom;
    private int status = 0;
    private Bag bag = new Bag(4);
    private HashMap<String, Person> team = new HashMap<>();
    private HashMap<String, Handler> handlers = new HashMap<>();
        
    public Game() 
    {
    	createHandlers();
        createRooms();
    }

	private void createRooms()
    {
    	Room outside, lobby, pub, study, bedroom, storage, stairs;
      
        //	制造房间
        outside = new Room("城堡外");
        lobby = new Room("大堂");
        pub = new Room("小酒吧");
        storage = new Room("储藏室");
        study = new Room("书房");
        bedroom = new Room("卧室");
        stairs = new Room("楼梯");
        
        //	初始化房间的出口
        outside.setDoubleExit("north", lobby);
        lobby.setDoubleExit("east", study);
        lobby.setDoubleExit("north", stairs);
        lobby.setDoubleExit("west", pub);
        study.setDoubleExit("north", bedroom);
        storage.setDoubleExit("east", stairs);
        stairs.setExit("up", new Room("二楼楼梯"), new Item(""));
        
        lobby.getExit("south").lock(new Item("大门钥匙"));
        stairs.getExit("west").lock(new Item("一张写着“晚餐放在储藏室”的备忘便笺"));
        
        //  初始化房间內的物品
        outside.addItems(new Item("花盆"));
        lobby.addItems(new Item("金币"), new Item("金币"));
        lobby.addPersons(new Person("管家") {

			@Override
			public void check() {
				System.out.println(this + "：哼哼哼，德古拉堡都敢随便乱闯。");
				System.out.println(this + "：今天就让你有来无回！");
			}
        	
        });

        pub.addPersons(new Person("店主"){

			@Override
			public void check() {
				System.out.println(this + "：买酒吗？");
			}

			@Override
			public boolean accept(Item item) {
				if (item.toString().equals("金币")){
					System.out.println(this + "：这是你的酒");
					getOutOfBag(item);
					addInBag(new Item("酒"));
					return true;
				}
				else{
					System.out.println(this + "：我不要");
					return false;
				}
			}
			
        });
        
        pub.addPersons(new Person("酒客"){

			@Override
			public void check() {
				System.out.println(this + "：哎呀，要是能再来一瓶就好了");
			}

			@Override
			public boolean accept(Item item) {
				getOutOfBag(item);
				if(item.toString().equals("酒")){					
					System.out.println(this + "：嗯……主人家总是有那么些小秘密，藏在……");
					System.out.println(this + "：嗝，书房里");
					return true;
				}
				else{
					System.out.println(this + "：唔……");
					return true;
				}
			}
			
        });
        
        study.addItems(new Item("书架") {

			@Override
			public Item get() {
				return new Item("一张写着“晚餐放在储藏室”的备忘便笺");
			}
        	
        }, new Item("金币"));
        
        storage.addItems(new Item("冷藏的红酒"));
        storage.addPersons(new Person("少女"){

			@Override
			public void check() {
				System.out.println(this + "：什么？你要和我一起走？");
			}

			@Override
			public boolean accept(Item item) {
				if (item.toString().equals("花盆")){
					System.out.println(this + "：啊哈~这个里面放着大门钥匙");
					getOutOfBag(item);
					addInBag(new Item("大门钥匙"));
					return true;
				}
				else if (item.toString().equals("冷藏的红酒")){
					getOutOfBag(item);
					System.out.println(this + "：哎呀真好喝");
					System.out.println(this + "：偷偷告诉你，卧室的床底下有暗门哦");
					System.out.println(this + "：要不我和你一块儿走好了");
					team.put("少女", this);
					currentRoom.removePerson(this);
					return true;
				}
				else{
					System.out.println(this + "：我不要");
					return false;
				}
			}
        });
        
        bedroom.addItems(new BackGroundItem("床") {

			@Override
			public void check() {
				bedroom.setExit("down", outside);
			}
        	
        });
        
        
        targetRoom = outside;
        currentRoom = outside;  //	从城堡门外开始
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("欢迎来到城堡！");
        System.out.println("这仍然是一个很无聊的游戏。");
        System.out.println("如果需要帮助，请输入 'help' 。");
        System.out.println();
        showPrompt();
    }
   
    private void addInBag( Item item){
		if ( item != null ){		
			if ( bag.add(item) )
				System.out.println("获得\t" + item); 
			else
				System.out.println("背包已满");
		}
    }
     
    private Item getOutOfBag (Item item){
		bag.remove(item);
    	if ( item != null ) 
    		System.out.println("失去\t" + item);
    	else
    		System.out.println("输入编号有误");
		return item;
    }
    
    public void showPrompt(){
        System.out.println("你在" + currentRoom + "，这里有");
        String str = null;
   
        if ( !(str = currentRoom.showItems()).equals("") ){
        	System.out.println("物品\t" + str);
        }
        if ( !(str = currentRoom.showPersons()).equals("") ){
        	System.out.println("人物\t" + str);
        }
        if ( !(str = currentRoom.showExits()).equals("") ){
        	System.out.println("出口\t" + str);
        }
    }

    private void createHandlers() {
    	
    	handlers.put("bye", new Handler() {

			@Override
			public boolean isBye() {
				return true;
			}

			@Override
			public void helpInfo() {
				System.out.println("bye：退出游戏");
			}
    		
    	});
    	
    	handlers.put("help", new Handler() {

			@Override
			public void doCmd(String word) {
				if ( word.equals("")){
					String[] handlerList = handlers.keySet().toArray(new String[handlers.size()]);
					Arrays.sort(handlerList);
					
					StringBuffer sb = new StringBuffer();
					for ( String str : handlerList ) {
						sb.append(str);
						sb.append(' ');
					}
					
			        System.out.println("迷路了吗？");
			        System.out.println("你可以做的命令有：" + sb.toString());
			        System.out.println("若需查看命令的详细信息，请输入'help 命令'，如'help help'");
				}
				else {
					Handler handler = handlers.get(word);
					if(handler != null)
						handler.helpInfo();
					else{
						System.out.println("命令不存在");
					}
				}
			}

			@Override
			public void helpInfo() {
				System.out.println("help：显示帮助信息");
				System.out.println("help 命令：显示该命令的帮助信息");
				System.out.println("如'help go'");
			}    		
			
    	});
    	
    	handlers.put("go", new Handler() {

			@Override
			public void doCmd(String word) {
				if ( word.equals("")){
					System.out.println("去哪儿？");
				}
				else{
			        Exit exit = currentRoom.getExit(word);
			        if (exit == null) {
			            System.out.println("一头撞在了墙上");
			        }
			        else {
			            Room nextRoom = exit.open();
			            if ( nextRoom == null ){
			            	System.out.println("好像上了锁");
			            	currentFeature = exit;
			            }
			            else{
			            	currentRoom = nextRoom;
			            	currentFeature = null;
			            	showPrompt();
			            	if ( currentRoom == targetRoom ){
			            		judge();
			            	}
			            }		            
			        }
				}
			}

			@Override
			public void helpInfo() {
				System.out.println("go 方向：向相应方向前进");
				System.out.println("如'go east'");
			}   		
			
    	});
    	
    	handlers.put("talk", new Handler(){

			@Override
			public void doCmd(String word) {
				try{
					currentFeature = currentRoom.getPerson(Integer.parseInt(word));
					currentFeature.check();
				} catch (NumberFormatException e) {
					System.out.println("talk后需跟数字");
				} catch (NullPointerException e) {
					System.out.println("输入的编号有误");
				}
			}

			@Override
			public void helpInfo() {
				System.out.println("talk 人物编号：和相应人物对话");
				System.out.println("如'talk 1'");
			}
    		
    	});
    	
    	handlers.put("check", new Handler(){

			@Override
			public void doCmd(String word) {
				try{
					currentFeature = currentRoom.getItem(Integer.parseInt(word));
					currentFeature.check();
				} catch (NumberFormatException e) {
					System.out.println("check后需跟数字");
				} catch (NullPointerException e) {
					System.out.println("输入的编号有误");
				}
			}

			@Override
			public void helpInfo() {
				System.out.println("check 物品编号：查看相应物品");
				System.out.println("如'check 1'");
			}
    		
    	});
        	
    	handlers.put("pick", new Handler() {
    		
    		@Override
    		public void doCmd(String word) {
    			try{
    				int index = Integer.parseInt(word);
    				Item item = currentRoom.getItem(index);
    				Item newItem = item.get();
    				if ( newItem.canBeInBag()) {
    					addInBag(newItem);
        				if (newItem == item){
        					currentRoom.removeItem(index);
        					if ( currentFeature == item )
        						currentFeature = null;
        				}
    				}
    				else{
    					System.out.println("这可装不进背包里");
    				}
    			} catch (NumberFormatException e){
    				System.out.println("pick后需跟数字");
    			} catch (NullPointerException e){
    				System.out.println("输入编号有误");
    			}  
    		}

			@Override
			public void helpInfo() {
				System.out.println("pick 物品编号：捡起物品，放入背包");
				System.out.println("如'pick 1'");
			}
    		
    	});
    	
    	handlers.put("bag", new Handler(){

			@Override
			public void doCmd(String word) {
				System.out.println(bag);
			}

			@Override
			public void helpInfo() {
				System.out.println("bag：查看背包");
			}
    		
    	});
    	
    	handlers.put("give", new Handler(){

			@Override
			public void doCmd(String word) {
				try{
					int index = Integer.parseInt(word);
					Item item = bag.get(index);
					currentFeature.accept(item);
				} catch (NumberFormatException e) {
					System.out.println("give后需跟数字");
				} catch (IndexOutOfBoundsException e) {
					System.out.println("输入编号有误");
				} catch (NullPointerException e ) {
					System.out.println("给谁呀？");
				}
			}

			@Override
			public void helpInfo() {
				System.out.println("give 物品编号：将背包中相应物品交给当前对象");
				System.out.println("如'give 1'");
			}
			
    	}); 
    	
    	handlers.put("here", new Handler(){

			@Override
			public void doCmd(String word) {
				showPrompt();
			}

			@Override
			public void helpInfo() {
				System.out.println("here：查看当前位置信息");
			}
    		
    	});
    	
	}

	private void judge() {
		if (team.containsKey("少女")){
			System.out.println("少女：又饿了……");
			System.out.println("少女：就吃了你吧");
			System.out.println("-游戏失败-");
			status = 2;
		}
		else{
			System.out.println("-恭喜逃脱-");
			status = 1;
		}
	}
	
    public void play(){
		Scanner in = new Scanner(System.in);
        while ( true ) {
    		String line = in.nextLine();
    		String[] words = line.split(" ");
    		Handler handler = handlers.get(words[0]);
    		String value = "";
    		if ( words.length > 1 )
    			value = words[1];
    		if ( handler != null ) {
    			handler.doCmd(value);
    			if ( handler.isBye() || status != 0) 
    				break;
    		}
    		else{
    			System.out.println("做不到哟");
    		}
        }
        in.close();
    }
    
	public static void main(String[] args) {
		Game game = new Game();
		game.printWelcome();
		game.play();
		
        System.out.println("-游戏结束-");
	}

}
