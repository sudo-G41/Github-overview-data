package com.sudoG41.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Test token 7ef2626899047aa3fc9bdc6b646b8168f6a9a587
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
public class GitData {
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("아이디가 없습니다.\n아이디를 입력해 주세요.");
            return;
        }
        for (String var : args) {
            GitData data = new GitData(var, 2);
            data.print();
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
    private int contributionsPerYear = 0;//사용불가...
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
    public GitData(){
    }
    /**
     * @param void not user?
     */
    public GitData(String str, int num){
        this.Authenticated(str, num);
        if(checkUesr){
            this.setNumOfProjectsParticipating();
            //this.setContributionsPerYear();
            this.setStarFork();
            this.setFollowers();
            this.setFollowing();
	        totalScore = this.getNumOfProjectsParticipating()+/*this.getContributionsPerYear()+*/this.getTotalStarred()+this.getTotalStarred()+this.getTotalFork()+(0.5*(this.getFollowers()+this.getFollowing()));
        }
    }

    /**
     * @param Token the user and repos to set using token Authorization
     */
    public void Authenticated(String str, int num){
        String apiUrl = "https://api.github.com/user";
        String token = "token "+str;
        URL url = null;
        HttpsURLConnection curl = null;
        InputStream is = null;
        Scanner urlScan = null;
        if(num == 1)
        {
            try
            {
                url = new URL(apiUrl);
                curl = (HttpsURLConnection)url.openConnection();
                curl.setRequestProperty("Authorization", token);
                is = curl.getInputStream();
            } catch (MalformedURLException e) 
            {
                //System.out.println("MalformedURLException on Authenticated");
            } catch (IOException e) 
            {
                //System.out.println("404 Not Found : This token not found");
                //System.out.println(token);
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
           setLogin();
           apiUrl = "https://api.github.com/users/"+getLogin();
        }
        else {
            setLogin(str);
            apiUrl = "https://api.github.com/users/"+str;
            try
            {
                url = new URL(apiUrl);
                curl = (HttpsURLConnection)url.openConnection();
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
        }
        
        try {
            url = new URL((apiUrl+"/repos"));
            curl = (HttpsURLConnection)url.openConnection();
            if(num == 1)
            {
                curl.setRequestProperty("Authorization", token);
            }
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
    //API가 변경되어 더이상 쓸수 없음...
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
