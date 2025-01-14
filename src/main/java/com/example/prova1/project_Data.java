package com.example.prova1;

public class project_Data {
    private String projectName="";
    private String IP = "";
    private String CIDR="";
    private String ROUTERS="";
    private String SWITCH="";
    private String SUBNET="";
    private String SUBNET_INFO="";
    public project_Data(String prn, String ip, String cidr,String ro,String sw,String subx,String subx_info){
        this.projectName = prn;
        this.IP = ip;
        this.CIDR = cidr;
        this.ROUTERS = ro;
        this.SWITCH = sw;
        this.SUBNET = subx;
        this.SUBNET_INFO = subx_info;
    }
    public String getProjectName(){
        return projectName;
    }
    public String getIP(){
        return IP;
    }
    public String getCIDR(){
        return CIDR;
    }
    public String getROUTERS(){
        return ROUTERS;
    }
    public String getSWITCH(){
        return SWITCH;
    }
    public String getSUBNET(){
        return SUBNET;
    }
    public String getSUBNET_INFO(){
        return SUBNET_INFO;
    }
    public String getALL(){
        return projectName+" "+IP+" "+CIDR+" "+ROUTERS+" "+SWITCH+" "+SUBNET+" "+SUBNET_INFO;
    }

}
