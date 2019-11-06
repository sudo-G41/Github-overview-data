import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Test token ac4adf5e7fd655f5c56170db08cd85dd225296e8
 * OSS에 대한 공헌 배점1점
 * -참여프로젝트 개수 완료
 * -년간 총 공헌 횟수
 * OSS 프젝트 인기 배점1점
 * -자신이 생성/소유한 repos가 starred된 횟수 완료
 * * -자신이 생성/소유한 repos가 fork된 횟수 완료
 * OSS 커뮤니티에 대한 관심 배점0.5점
 * -Following 수 완료
 * -Followers 수 완료
 * Maker sucheol Jeong
 * Departmen of Unification Theory
 * ver j.0.1
 * 2019-10-03
 */
public class GitDataFile {
    public static void main(String[] args) {
        File f, fSave;
        Scanner s = null, sc = null;
        BufferedWriter bw = null;
        GitDataFile data;
        String id;
        int idnum = -1, studentIdnum = -1, namenum = -1;
        boolean save = true;
        if(args.length == 0){
            System.out.println("아이디가 없습니다.\n아이디를 입력해 주세요.");
            return;
        }
        for (String var : args) {
            if(var.indexOf(".csv") > 0){
                try {
                    while (save) {
                        System.out.println("정보를 어디에 저장할까요?");
                        System.out.println("1. 기존 파일에 쓰기(양식에 맞는 .csv파일)");
                        System.out.println("2. 새로 만들기(Github Data.csv생성)");
                        sc = new Scanner(System.in);
                        int num = sc.nextInt();
                        if(num == 1){
                            sc.nextLine();
                            String tmp = sc.nextLine();
                            if(tmp.indexOf(".csv")<0)tmp = tmp+".csv";
                            fSave = new File(tmp);
                            bw = new BufferedWriter(new FileWriter(fSave, true));
                            save = false;
                            sc.close();
                            break;
                        }else if (num == 2) {
                            fSave = new File("Github Data.csv");
                            bw = new BufferedWriter(new FileWriter(fSave));
                            bw.write("이름,학번,Github ID,참여 프로젝트 개수,Github 공헌 횟수");
                            bw.write(",본인 repos Starred,본인 repos fork,Followers 수,Following 수,총합 점수\n");
                            save = false;
                            sc.close();
                            break;
                        }else{
                            System.out.println("???????????????????????");
                        }
                        System.out.println("?");
                        Thread.sleep(1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e){
                }
                try {
                    System.out.println("File");
                    f = new File(var);
                    s = new Scanner(f);
                    // s.useDelimiter("\n");
                    if(s.hasNext()) {
                        // System.out.println(s.nextLine());
                        id = s.nextLine();
                        String[] tmp = id.split(",");
                        for (int i = 0; i < tmp.length; i++) {
                            if (tmp[i].equals("이름")){
                                namenum = i;
                            }else if (tmp[i].equals("학번")){
                                studentIdnum = i;
                            }else if(tmp[i].equals("Github ID")){
                                idnum = i;
                            }
                        }
                        System.out.println("idnumn:"+idnum);
                        while(s.hasNext()){
                            id = s.nextLine();
                            tmp = id.split(",");
                            System.out.println("id:"+tmp[idnum]);
                            data = new GitDataFile(tmp[idnum]);
                            bw.write(tmp[namenum]+","+tmp[studentIdnum]+","+tmp[idnum]+",");
                            bw.write(""+data.getNumOfProjectsParticipating()+",");
                            bw.write(""+data.getContributionsPerYear()+",");
                            bw.write(""+data.getTotalStarred()+",");
                            bw.write(""+data.getTotalFork()+",");
                            bw.write(""+data.getFollowers()+",");
                            bw.write(""+data.getFollowing()+",");
                            bw.write(""+data.getTotalScore()+"\n");
                            data.print();
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(var+"이란 파일이 없습니다.");
                } catch (NoSuchElementException e) {
                    System.out.println("...???");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("양식에 맞는 파일을 사용해 주시기 바랍니다.");
                } catch (IOException e){
                    e.printStackTrace();
                }
                s.close();
                sc.close();
            }
            else{
                System.out.println("id");
                data = new GitDataFile(var);
                data.print();
            }
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 
     * String login 
     * -Id저장 
     * -getLogin()
     * -setLogin()
     * -setLogin(string)
     * 
     * int NumOfProjectsParticipating 
     * -Oss참여 프로잭트 수
     * -getNumOfProjectsParticipating()
     * -setNumOfProjectsParticipating()
     * 
     * int contributionsPerYear
     * -연간 활동량
     * -getContributionsPerYear()
     * -setContributionsPerYear()
     * 
     * int totalStarred
     * -내 프로잭트가 스타 된 수
     * -getTotalStarred()
     * -setStarFork()
     * int totalFork
     * -내 프로잭트가 포크 된 수
     * -getTotalFork()
     * -setStarFork()
     * 
     * int followers
     * -팔로워 수
     * -getFollowers()
     * -setFollowers()
     * int following 
     * -팔로잉 수
     * -getFollowing()
     * -setFollowing()
     * 
     * String user
     * -유저 정보
     * -getUser()
     * String repos
     * -유저 repos정보
     * -getRepos()
     * 위의 둘은 Authenticated(token(또는 Id), (1은 토큰 2는 아이디))에서 생성됨
     * 
     * boolean checkUesr
     * -유저가 있는지 채크 해줌
     * -getCheckUesr()
     * -setCheckUesr()
     * 
     * double totalScore
     * -최종 점수
     * -Oss참여 프로잭트 수당 1점
     * -연간 활동량당 1점
     * -프로잭트가 스타 된 수당 1점
     * -내 프로잭트가 포크 된 수당 1점
     * -팔로워당 0.5점
     * -팔로잉당 0.5점
     * -getTotalScore()
     */
    private String login = null;
    private int NumOfProjectsParticipating = 0;
    private int contributionsPerYear = 0;
    private int totalStarred = 0;
    private int totalFork = 0;
    private int followers = 0;
    private int following = 0;
    private String user = null;
    private String repos = null;
    private boolean checkUesr = true;
    private double totalScore = 0.0;
    /**
     * @param void not user?
     */
    public GitDataFile(){
    }
    /**
     * @param void not user?
     */
    public GitDataFile(String str){
        this.Authenticated(str);
        if(checkUesr){
            this.setNumOfProjectsParticipating();
            this.setContributionsPerYear();
            this.setStarFork();
            this.setFollowers();
            this.setFollowing();
	        totalScore = this.getNumOfProjectsParticipating()+this.getContributionsPerYear()+this.getTotalStarred()+this.getTotalStarred()+this.getTotalFork()+(0.5*(this.getFollowers()+this.getFollowing()));
        }
    }

    /**
     * @param Token the user and repos to set using token Authorization
     */
    public void Authenticated(String str){
        String apiUrl = "https://api.github.com/user";
        URL url = null;
        HttpsURLConnection curl = null;
        InputStream is = null;
        Scanner urlScan = null;

        setLogin(str);
        apiUrl = "https://api.github.com/users/"+str;
        try
        {
            url = new URL(apiUrl);
            curl = (HttpsURLConnection)url.openConnection();
            curl.addRequestProperty("Authorization", "token 35330bb0c51001d862e95fe4e99553c06020ded7");
            is = curl.getInputStream();
        } catch (MalformedURLException e) 
        {
        } catch (IOException e) 
        {
            setCheckUesr(false);
            return;
        }
        urlScan = new Scanner(is);

        if(urlScan.hasNextLine())
        {
            do 
            {
                user = urlScan.nextLine();
            } while (urlScan.hasNextLine());
        }
        
        try {
            url = new URL((apiUrl+"/repos"));
            curl = (HttpsURLConnection)url.openConnection();
            curl.setRequestProperty("Authorization", "token 35330bb0c51001d862e95fe4e99553c06020ded7");
            is = curl.getInputStream();
        } catch (MalformedURLException e) {
            //System.out.println("MalformedURLException on repos");
        } catch (IOException e) {
            //System.out.println("IOException on repos");
        }
        urlScan = new Scanner(is);
        
        if(urlScan.hasNextLine()){
            do {
                this.repos = urlScan.nextLine();
            } while (urlScan.hasNextLine());
        }
        urlScan.close();
        curl.disconnect();
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(){
        int start = this.user.indexOf("login")+8;
        int end = this.user.indexOf("\",\"id");
        setLogin(this.user.substring(start, end));
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public int getNumOfProjectsParticipating() {
        return NumOfProjectsParticipating;
    }
    public void setNumOfProjectsParticipating() {
        int public_start = 0;
        int public_end = 0;
        int private_start = 0;
        int private_end = 0;
        int x = 0;
        if(user.indexOf("public_repos\":") > -1)
        {
            public_start = user.indexOf("public_repos\":")+"public_repos\":".length();
            public_end = this.user.indexOf(",", public_start);
            x = Integer.parseInt(this.user.substring(public_start, public_end));
        }
        if(user.indexOf("total_private_repos\":",public_end) > -1)
        {
            private_start = user.indexOf("total_private_repos\":")+"total_private_repos\":".length();
            private_end = user.indexOf(",", private_start);
            x += Integer.parseInt(this.user.substring(private_start, private_end));
        }
        this.NumOfProjectsParticipating = x;
    }

    public int getContributionsPerYear() {
        return contributionsPerYear;
    }
    public void setContributionsPerYear() {
        BufferedReader buffer = null;
        InputStream is = null;
        URL url = null;
        HttpsURLConnection curl = null;
        String gitApi = "https://github-contributions-api.now.sh/v1/"+this.login;
        String tmp = null;
        int sum = 0;
        try {
            url = new URL(gitApi);
            curl = (HttpsURLConnection)url.openConnection();
            is = curl.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(is));
            while (buffer.ready()) {
                tmp = buffer.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception URL:"+ gitApi, e);
        }
        String[] str = tmp.split("\\[|\\]");
        tmp = str[1];
        /*
        str = tmp.split("\\},\\{");
        for (String var : str) {
            int i = var.indexOf("\"total\"");
            if(i == -1){break;}
            int istart = var.indexOf(":", i)+1;
            int iend = var.indexOf(",", i);
            sum += Integer.parseInt(var.substring(istart, iend));
        }
        */
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
        int x = calendar.get(Calendar.YEAR);
        String s = "year\":\""+x+"\",\"total\":";
        int istart = tmp.indexOf(s)+s.length();
        if(istart > -1 && tmp.length() > 0){
            int iend = tmp.indexOf(",", istart);
            sum += Integer.parseInt(tmp.substring(istart, iend));
        }
        this.contributionsPerYear = sum;
        try {
            buffer.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
        curl.disconnect();
    }

    public int getTotalStarred() {
        return totalStarred;
    }
    public int getTotalFork() {
        return totalFork;
    }
    public void setStarFork() {
        String[] str = this.repos.split("\\},\\{");
        int sumStar = 0;
        int sumFork = 0;
        try {
            for (String var : str) {
                int i = var.indexOf("\"fork\"");
                int is = var.indexOf(":", i)+1;
                int ie = var.indexOf(",", i);
                boolean bool = Boolean.parseBoolean(var.substring(is, ie));
                if(!bool){
                    i = var.indexOf("\"stargazers_count\"");
                    is = var.indexOf(":", i)+1;
                    ie = var.indexOf(",", i);
                    sumStar += Integer.parseInt(var.substring(is, ie));
                    i = var.indexOf("\"forks_count\"");
                    is = var.indexOf(":", i)+1;
                    ie = var.indexOf(",", i);
                    sumFork += Integer.parseInt(var.substring(is, ie));
                }
            }
        } catch(StringIndexOutOfBoundsException e){
            //System.out.println("======================");
            //System.out.println("|have no Repositories|");
            //System.out.println("======================");
        } catch (Exception e) {
            //TODO: handle exception
        }
        this.totalStarred = sumStar;
        this.totalFork = sumFork;
    }

    public int getFollowers() {
        return followers;
    }
    public void setFollowers() {
        int start = this.user.indexOf("followers\":")+11;
        int end = this.user.indexOf(",", start);
        int x = Integer.parseInt(this.user.substring(start, end));
        this.followers = x;
    }

    public int getFollowing() {
        return following;
    }
    public void setFollowing() {
        int start = this.user.indexOf("following\":")+11;
        int end = this.user.indexOf(",", start);
        int x = Integer.parseInt(this.user.substring(start, end));
        this.following = x;
    }

    public String getUser() {
        return user;
    }

    public String getRepos() {
        return repos;
    }

    public boolean getCheckUesr(){
        return this.checkUesr;
    }
    
    public void setCheckUesr(boolean checkUesr) {
        this.checkUesr = checkUesr;
    }

    public double getTotalScore(){
	    return totalScore;
    }

    public void print(){
        System.out.println("Id:"+ login);
        System.out.println("participating:"+ NumOfProjectsParticipating);
        System.out.println("Activity:"+ contributionsPerYear);
        System.out.println("Total repos Starred:"+ totalStarred);
        System.out.println("Total repos fork:"+ totalFork);
        System.out.println("Followers:"+ followers);
        System.out.println("Following:"+ following);
        System.out.println("totalScore:"+ totalScore);
    }
}
