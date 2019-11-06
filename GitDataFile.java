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
 * OSS�� ���� ���� ����1��
 * -����������Ʈ ���� �Ϸ�
 * -�Ⱓ �� ���� Ƚ��
 * OSS ����Ʈ �α� ����1��
 * -�ڽ��� ����/������ repos�� starred�� Ƚ�� �Ϸ�
 * * -�ڽ��� ����/������ repos�� fork�� Ƚ�� �Ϸ�
 * OSS Ŀ�´�Ƽ�� ���� ���� ����0.5��
 * -Following �� �Ϸ�
 * -Followers �� �Ϸ�
 * Maker sucheol Jeong
 * Departmen of Unification Theory
 * ver j.0.1
 * 2019-10-03
 * �ɼ�
 * -help ����
 * -f input ������ ���Ϸ� ������ �����Ǹ� ���̵�� ����
 */
public class GitDataFile {
    public static void main(String[] args) {
        File f, fSave;
        Scanner s = null;
        BufferedWriter bw = null;
        GitDataFile data;
        String id;
        boolean ob = false, boolFile;
        if(args.length == 0){
            System.out.println("���̵� �����ϴ�.\n���̵� �Է��� �ּ���.");
            return;
        }
        if(args[0].equals("-help")){
            System.out.println("����: java GitDataFile Github id �Ǵ� ���ϸ�(Ȯ���� ����) [args...]");
            return;
        }
        try {
            fSave = new File("Github Data.csv");
            boolFile = !fSave.exists();
            bw = new BufferedWriter(new FileWriter(fSave,true));
            if (boolFile) {
                bw.write("Github ID,���� ������Ʈ ����,Github ���� Ƚ��");
                bw.write(",���� repos Starred,���� repos fork,Followers ��,Following ��,���� ����\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
        }
        for (String var : args) {
            if(var.equals("-f")) ob = true;
        }
        if(ob){
            String[] tmp;
            try {
                // System.out.println("File");
                String var;
                if(args[0].equals("-f")){
                    var = args[1];
                }else {
                    var = args[0];
                }
                f = new File(var);
                s = new Scanner(f);
                // s.useDelimiter("\n");
                if(s.hasNext()) {
                    // System.out.println(s.nextLine());
                    // id = s.nextLine();
                    // System.out.println("idnumn:"+idnum);
                    while(s.hasNext()){
                        id = s.nextLine();
                        tmp = id.split(",| |\t");
                        for (int i = 0; i < tmp.length; i++) {
                            data = new GitDataFile(tmp[i]);
                            if(data.getRepos() == null){
                                System.out.println("==========================");
                                System.out.println(tmp[i]+"�� ���� ID�Դϴ�. "+i+"��° ���̵�");
                                System.out.println("==========================");
                            }
                            else{
                                bw.write(tmp[i]+",");
                                bw.write(""+data.getNumOfProjectsParticipating()+",");
                                bw.write(""+data.getContributionsPerYear()+",");
                                bw.write(""+data.getTotalStarred()+",");
                                bw.write(""+data.getTotalFork()+",");
                                bw.write(""+data.getFollowers()+",");
                                bw.write(""+data.getFollowing()+",");
                                bw.write(""+data.getTotalScore()+"\n");
                                System.out.println("==========================");
                                data.print();
                                System.out.println("==========================");
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                String var = args[0].equals("-f") ? args[1]:args[0];
                System.out.println("\""+var+"\""+" �̶� ������ �����ϴ�.");
                return;
            } catch (NoSuchElementException e) {
                System.out.println("...???");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("��Ŀ� �´� ������ ����� �ֽñ� �ٶ��ϴ�.");
            } catch (IOException e){
                e.printStackTrace();
            }
            s.close();
        }
        else{
            // System.out.println("id");
            for (String var : args) {
                data = new GitDataFile(var);
                if(data.getRepos() == null){
                    System.out.println("==========================");
                    System.out.println("���� ID�Դϴ�.");
                    System.out.println("==========================");
                }else{
                    try {
                        bw.write(var+",");
                        bw.write(""+data.getNumOfProjectsParticipating()+",");
                        bw.write(""+data.getContributionsPerYear()+",");
                        bw.write(""+data.getTotalStarred()+",");
                        bw.write(""+data.getTotalFork()+",");
                        bw.write(""+data.getFollowers()+",");
                        bw.write(""+data.getFollowing()+",");
                        bw.write(""+data.getTotalScore()+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("==========================");
                    data.print();
                    System.out.println("==========================");
                }
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     * String login 
     * -Id���� 
     * -getLogin()
     * -setLogin()
     * -setLogin(string)
     * 
     * int NumOfProjectsParticipating 
     * -Oss���� ������Ʈ ��
     * -getNumOfProjectsParticipating()
     * -setNumOfProjectsParticipating()
     * 
     * int contributionsPerYear
     * -���� Ȱ����
     * -getContributionsPerYear()
     * -setContributionsPerYear()
     * 
     * int totalStarred
     * -�� ������Ʈ�� ��Ÿ �� ��
     * -getTotalStarred()
     * -setStarFork()
     * int totalFork
     * -�� ������Ʈ�� ��ũ �� ��
     * -getTotalFork()
     * -setStarFork()
     * 
     * int followers
     * -�ȷο� ��
     * -getFollowers()
     * -setFollowers()
     * int following 
     * -�ȷ��� ��
     * -getFollowing()
     * -setFollowing()
     * 
     * String user
     * -���� ����
     * -getUser()
     * String repos
     * -���� repos����
     * -getRepos()
     * ���� ���� Authenticated(token(�Ǵ� Id), (1�� ��ū 2�� ���̵�))���� ������
     * 
     * boolean checkUesr
     * -������ �ִ��� äũ ����
     * -getCheckUesr()
     * -setCheckUesr()
     * 
     * double totalScore
     * -���� ����
     * -Oss���� ������Ʈ ���� 1��
     * -���� Ȱ������ 1��
     * -������Ʈ�� ��Ÿ �� ���� 1��
     * -�� ������Ʈ�� ��ũ �� ���� 1��
     * -�ȷο��� 0.5��
     * -�ȷ��״� 0.5��
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
