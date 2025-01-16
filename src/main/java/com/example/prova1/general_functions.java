package com.example.prova1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.control.Alert.AlertType;
import jdk.swing.interop.SwingInterOpUtils;

public class general_functions implements Initializable {
    //variables and objects
    @FXML //buttons
    private Button lan_info_home_btn, home_menu_lan_info,cip,calcularIP_tohome_btn;
    @FXML
    private TextField ruta_projecte,n_projecte,ip_xarxa,n_routers,n_switch,n_subxarxes;
    @FXML //choicebox
    private ChoiceBox mascares;
    @FXML
    private TextArea names_users_subx,report_area;


    //existing project data aux START
    String projectName="";
    String IP = "";
    String CIDR="";
    String ROUTERS="";
    String SWITCH="";
    String SUBNET="";
    String SUBNET_INFO="";
    //existing project data aux END

    //REGULAR EXPRESSION (file/info checks)
        //info checks
    String empty_textfield= "[^\\d\\w\s]"; // comprova que no hu hagui ningun caracter o numero
    String is_number = "\\d+"; // comprova que nomes hi hagui numeros
    String is_IP = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$"; // comprova si es una IP valida
    String istxt = "[^\\W]\\w+\\.txt$";
    String corrtctTextArea= "[\\w]+:[\\s|\\t]*[\\d]+[^\\W]";
        //file checks
    String IP_check = "^IP_DE_XARXA=\\d{1,3}(\\.\\d{1,3}){3}$"; //selecciona el format "IP_DE_XARXA=192.168.16.0" (no comprova si la IP es valida)
    String cidr_check = "^MASCARA_DE_XARXA=\\/(30|[12]?[1-9])$"; // selecciona el format "MASCARA_DE_XARXA=/24"
    String general_info_check = "(ROUTERS|SWITCH|SUBXARXA)=[\\d]"; // selecciona un format recurrent que sorgeix en el fitxer creat en LAN INFO

    // for scene changes
    private Stage stage;
    private Scene scene;
    private Parent root;

    //objects
    DirectoryChooser dc = new DirectoryChooser(); //obre finestres del buscador d'arxius
    project_Data PROJECT_DATA;


    //functions
    /**
     * inicialitza totes aquelles funcions que es necessari que s'implementin al principi de tot, just despres de que el programa s'executi
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialize triggered");
        fillmask();
    }
    /**
     * Omple el ChoiceBox amb les opcions disponibles de màscares de xarxa, des de /1 fins a /30.
     */
    @FXML
    public void fillmask() {
        System.out.println("fillmask triggered");
        if (mascares == null) {
            System.out.println("Error: mascares no està inicialitzat o no esta en el formulari adecuat");
            return;
        }
        for (int i = 1; i <= 30; i++) {
            mascares.getItems().add("/" + i);
        }
        System.out.println("Opcions afegides al ChoiceBox");
    }

    /**
     * Aquesta funció servira per indicar missatges rapids a l'usuari per a donar informació
     * @param message
     */
    public void popupINFO(String message, String header) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informació");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait(); // Mostra el popup i espera fins que l'usuari tanqui la finestra
    }
    /**
     * Aquesta funció servira per indicar missatges rapids a l'usuari per a alertar de algun possible error
     * @param message
     */
    public void popupWARNING(String message, String header) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Informació");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait(); // Mostra el popup i espera fins que l'usuari tanqui la finestra
    }
    /**
     * Aquesta funció servira per indicar missatges rapids a l'usuari per a alertar a l'usuari d'un error
     * @param message
     */
    public void popupERROR(String message, String header) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait(); // Mostra el popup i espera fins que l'usuari tanqui la finestra
    }
    @FXML
    /**
     * Tanca l'aplicació completament.
     */
    protected void exit(ActionEvent event){System.exit(0);}

    // navigation functions

    /**
     * Canvia entre formularis segons el fitxer FXML especificat. Carrega el nou formulari i actualitza la interfície gràfica.
     * @param fxmlFile
     * @param event
     */
    public void formswitch(ActionEvent event,String fxmlFile) throws IOException {

        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML file: " + fxmlFile);
        }
    }

    /**
     * Gestiona la navegació entre formularis depenent del botó que s'hagi clicat.
     * @param event
     * @throws IOException
     */
    public void sceneBuild(ActionEvent event) throws IOException {

        if (event.getSource() == lan_info_home_btn){ // boto per anar des del menu principal al formulari LAN INFO.
            formswitch(event, "LAN_INFO.fxml");
        }else if(event.getSource() == home_menu_lan_info){ // boto de LAN_INFO. al menu principal
            formswitch(event,"hello-view.fxml");
        }else if (event.getSource() == cip) {
            formswitch(event,"CalcularIP_submenu.fxml");
        }else if (event.getSource() == calcularIP_tohome_btn) {
            formswitch(event,"hello-view.fxml");
        }

    }

    /**
     * Obre un selector de directoris i assigna la ruta seleccionada al camp de text corresponent
     * @param event
     */
    public void setDirectory(ActionEvent event){
        File selectedDirectory = dc.showDialog((Stage) ((Node) event.getSource()).getScene().getWindow());
        ruta_projecte.setText(selectedDirectory.toPath().toString());
    }
    /**
     * Valida si una adreça IP es una ip de xarxa correcta segons la IP i el CIDR que es proveerix
     *
     * @param ip
     * @param cidr
     */
    public boolean isvalidIP (String ip, String cidr){
        System.out.println("isvalidIP triggered");
        String[] segmentedIP = ip.split("\\.");
        String[] segmentedCIDR = cidr.split("/");
        StringBuilder binaryIP = new StringBuilder();
        StringBuilder binaryCIDR = new StringBuilder();
        boolean isValid=false;
        for(String i : segmentedIP){
            int value = Integer.parseInt(i);
            String binaryOctet = String.format("%08d", Integer.parseInt(Integer.toBinaryString(value))); // Asegura 8 bits
            binaryIP.append(binaryOctet);
        }
        for (int i = 0; i < 32; i++) {
            if (i < Integer.valueOf(segmentedCIDR[1])) {
                binaryCIDR.append("1");
            } else {
                binaryCIDR.append("0");
            }
        }
        for (int i = 0; i < 32; i++) {
            if(binaryCIDR.charAt(i) != '1'){
                if (binaryCIDR.charAt(i) == binaryIP.charAt(i)){
                    isValid=true;
                }else{
                    isValid=false;
                    break;
                }
            }
        }
        return isValid;
    }
    /**
     * Comprova si les dades d'entrada compleixen amb els criteris de validació segons el tipus especificat.
     *
     * <p>Els tipus possibles són:</p>
     * <ul>
     *     <li><b>file</b>: Verifica que la cadena representa un fitxer amb extensió .txt.</li>
     *     <li><b>ip</b>: Valida si la cadena és una adreça IP vàlida en format IPv4.</li>
     *     <li><b>path</b>: Comprova si la cadena correspon a un directori existent en el sistema.</li>
     *     <li><b>general</b>: Verifica si la cadena només conté números.</li>
     *     <li><b>cidr</b>: Accepta qualsevol cadena que no sigui buida o tingui caràcters especials.</li>
     *     <li><b>area</b>: Comprova que el textArea estigui amb elformat demanat, correspongui amb el nombre de subxarxes i no es repeteixin noms</li>
     * </ul>
     *
     * @param data     la dada a validar segons el tipus especificat.
     * @param datatype el tipus de validació a aplicar (file, ip, path, general, cidr).
     * @return {@code true} si la dada compleix amb els criteris del tipus especificat; {@code false} altrament.
     * @throws NullPointerException si algun dels paràmetres d'entrada és {@code null}.
     * @throws IllegalArgumentException si el tipus especificat no és vàlid.
     */
    public boolean lan_info_error_check(String data, String datatype){

        boolean isValid=false;
        switch (datatype){
            case "file":
                if (!data.matches(empty_textfield) && data.matches(istxt)){
                    isValid=true;
                }
                break;
            case "ip":
                if (!data.matches(empty_textfield) && data.matches(is_IP)){
                    isValid=true;
                }
                break;
            case "path":
                File file = new File(data);
                if (file.exists() && file.isDirectory()){
                    isValid=true;
                }
                break;
            case "general":
                if (!data.matches(empty_textfield) && data.matches(is_number)){
                    isValid=true;
                }
                break;
            case "cidr":
                if (!data.matches(empty_textfield)){
                    isValid=true;
                }
                break;
            case "area":
                String[] lines = data.split("\\R");
                Set<String> nom = new HashSet<>(); // un set no deixa afegir noms repetits
                if (lines.length != Integer.valueOf(n_subxarxes.getText())){
                    break;
                }
                for (String i : lines){
                    if (i.matches(corrtctTextArea)){
                        String[] segmentedFormat=i.split(":");
                        isValid=true;
                        if (!nom.add(segmentedFormat[0])){
                            isValid=false;
                            break;
                        }
                    }
                }
                nom.clear();
                break;
        }
        return isValid;
    }

    /**
     * aquesta funció depura els errors i dona un popup d'error en el cas que n'i hagui
     * @param error
     */
    public void lan_info_errors_found(ArrayList<String> error){
        String errorsfound="Sembla que hi ha un error en els següents camps:\n";
        String giveinfo="";
        for(String i : error){
            switch (i){
                case "file":
                    errorsfound+="Nom del Projecte\n";
                    giveinfo+="Nom del Projecte:\n" +
                            "Torna a mirar que s'hagui escrit correctament (ha s'acabar amb .txt i no pot contenir caracters especials)\n";
                    break;
                case "path":
                    errorsfound+="Ruta del Projecte\n";
                    giveinfo+="Ruta del Projecte:\n" +
                            "Es possible que el la ruta s'hagui modificat o no existeixi, comprova-la\n";
                    break;
                case "ip":
                    errorsfound+="IP de xarxa\n";
                    giveinfo+="IP de xarxa:\n" +
                            "No es una IP de xarxa o no es una IP valida, comprova-ho una altra vegada\n";
                    break;
                case "ruter":
                    errorsfound+="Nº routers\n";
                    giveinfo+="Nº routers:\n" +
                            "Ha de ser un número enter o el numero es massa llarg\n";
                    break;
                case "sw":
                    errorsfound+="Nº Switch\n";
                    giveinfo+="Nº Switch:\n" +
                            "Ha de ser un número enter o el numero es massa llarg\n";
                    break;
                case "area":
                    errorsfound+="Nº ordenadors per Subxarxa\n";
                    giveinfo+="Nº ordenadors per Subxarxa:\n" +
                            "No es el format correcte (Nom: nºusuaris),\nel numero es massa llarg\nno correspon a les subxarxes indicades\n";
                    break;
                case "subx":
                    errorsfound+="Nº subxarxes\n";
                    giveinfo+="Nº subxarxes:\n" +
                            "Ha de ser un número enter o el numero es massa llarg\n";
                    break;
                case "null":
                    errorsfound+="Falten camps per omplir\n";
                    break;
                case "xarxa":
                    errorsfound+="IP de xarxa(IP no valida)\n";
                    giveinfo+="IP de xarxa(IP no valida):\n" +
                            "La IP que has povehit no es de xarxa revisa les mascares\n";
                    break;
            }
        }
        popupERROR(errorsfound, "ERRORS TROVATS");
        if (!giveinfo.equals("")){
            popupINFO(giveinfo, "Informació a tenir en compte");
        }
    }

    /**
     * comproba si el fitxer a crear existeix o no i retorna un objecte File amb la ruta i el nom passats per parametre
     * en el cas de que l'arxiu que es vol crear no existeixi
     * @param path
     * @param filename
     * @return
     */
    public File Filecheck(String path, String filename){
        File wheresave = new File(path);

        File[] fileList = wheresave.listFiles();
        Boolean exists = false;
        for(File i : fileList){
            if(i.getName().equals(filename)){
                exists=true;
                break;
            }
        }
        if (exists){
            String errormessage = "El Fitxer : "+filename+" en la ruta:"+path+" Ja esta creat.";
            popupERROR(errormessage,"Error de fitxers");
        }else {
            File filetocreate = new File(path,filename);
            return filetocreate;
        }
        return wheresave;
    }
    /**
     * Valida tots els camps d'entrada de la pantalla LAN INFO i genera un informe d'errors si n'hi ha. Si tots els camps són vàlids, guarda la informació.
     */
    public void lan_info_save() throws IOException {
        System.out.println("Lan_Info_save trigered");
        ArrayList<String> errors = new ArrayList<String>();
        String file = n_projecte.getText();
        String ruta = ruta_projecte.getText();
        String ip = ip_xarxa.getText();
        String cidr = (String) mascares.getValue();
        String ruters = n_routers.getText();
        String sw = n_switch.getText();
        String area = names_users_subx.getText();
        String subx = n_subxarxes.getText();

        //comprova que els textfields no estiguin buits
        if (file != null || ruta != null || ip != null || cidr != null || ruters != null || sw != null || area != null || subx != null){
            if(!lan_info_error_check(ruta,"path")){//comprova si el path existeix
                errors.add("path");
            }
            if (!lan_info_error_check(file, "file")){ //comproba si el fitxer esta ben escrit i no esta buit
                errors.add("file");
            }
            if(!lan_info_error_check(ip,"ip")){ //comprova si una IP es valida
                errors.add("ip");
            }
            if (!lan_info_error_check(cidr,"cidr")){ //comprova que s'hagui escollit una mascara
                errors.add("cidr");
            }
            if (!lan_info_error_check(ruters,"general")){ // comprova que s'hagui escrit un número
                errors.add("ruter");
            }
            if (!lan_info_error_check(sw,"general")){ // comprova que s'hagui escrit un número
                errors.add("sw");
            }
            if (!lan_info_error_check(area,"area")){ // comprova que cada linea del TextArea sigui el format correcte
                errors.add("area");
            }
            if (!lan_info_error_check(subx,"general")){ // comprova que s'hagui escrit un número
                errors.add("subx");
            }
            if(!isvalidIP(ip,cidr)){ // comprova que la IP entrada sigui una IP de xarxa
                errors.add("xarxa");
            }
        }else{
            errors.add("null");
        }
        if (!errors.isEmpty()){
            lan_info_errors_found(errors);
        }else{

            popupWARNING("No es noten errors\nEs guardara aquesta informació", "AVÍS");
            File parameters = Filecheck(ruta,file);
            parameters.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(parameters, true))) { // 'true' para agregar contenido sin sobrescribir
                writer.write("IP_DE_XARXA="+ip);
                writer.newLine();
                writer.write("MASCARA_DE_XARXA="+cidr);
                writer.newLine();
                writer.write("ROUTERS="+ruters);
                writer.newLine();
                writer.write("SWITCH="+sw);
                writer.newLine();
                writer.write("SUBXARXA="+subx);
                writer.newLine();
                writer.write("SUBXARXA_INFO=\n"+area);

                System.out.println("Contingut guardat amb exit");
                writer.close();
            } catch (IOException e) {
                System.out.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
            }
        }
    }

    /**
     * aquesta funcio comprovara que el projecte ja creat continui amb el format que el programa accepta i en el cas que
     * el format sigui acceptat el guardara en el ArrayList PROJECT_DATA
     * @param project
     * @return
     */
    public boolean is_project_valid(File project){
        boolean isValid = false;
        String errors ="";
        try (BufferedReader br = new BufferedReader(new FileReader(project))) {
            String linea;
            int numero_linea=1;
            Set<String> nom = new HashSet<>();
            while ((linea = br.readLine()) != null) {
                switch (numero_linea){
                    case 1:
                        isValid = linea.matches(IP_check);
                        break;
                    case 2:
                        isValid = linea.matches(cidr_check);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        isValid = linea.matches(general_info_check);
                        break;
                    case 6:
                        isValid = linea.equals("SUBXARXA_INFO=");
                        break;
                    default:
                        if (linea.matches("^[a-zA-Z0-9_]+:[\\s|\\t]*[0-9]+$")){
                            String[] segmentedFormat=linea.split(":");
                            isValid=nom.add(segmentedFormat[0]);
                            break;
                        }else{
                            isValid=false;
                        }
                }
                System.out.println(linea+"  "+ isValid);
                if (!isValid){ //afegeix les linies que tenen errors
                    errors+="Error en la linea: "+numero_linea+"\n";
                    break;
                }else{
                    switch (numero_linea){
                        case 1:
                            IP =linea.replace("IP_DE_XARXA=","");
                            break;
                        case 2:
                            CIDR=linea.replace("MASCARA_DE_XARXA=","");
                            break;
                        case 3:
                            ROUTERS=linea.replace("ROUTERS=","");
                            break;
                        case 4:
                            SWITCH=linea.replace("SWITCH=","");
                            break;
                        case 5:
                            SUBNET=linea.replace("SUBXARXA=","");
                            break;
                        case 6:
                            break;
                        default:
                            SUBNET_INFO+=linea+"\n";
                    }
                }

                numero_linea++;
            }
        } catch (IOException e) {
            System.err.println("HI ha problemes amb l'arxiu: " + e.getMessage());
        }
        if (!errors.equals("")){
            popupERROR(errors,"Error");
        }

        return isValid;
    }
    /**
     * funció que selecciona el fitxer contenint un projecte ja creat en la opció LAN INFO. per a carregar les dades
     * i fer-les servir per a les altres opcións disponibles
     * @param event
     */
    public void project_search(ActionEvent event){
        System.out.println("project_search triggered");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt") //indica quin tipus de fitxer acceptara
        );
        //obre la finestra per a buscar
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null && is_project_valid(selectedFile)) {
            projectName=selectedFile.getName().replace(".txt","");
            PROJECT_DATA = new project_Data(projectName,IP,CIDR,ROUTERS,SWITCH,SUBNET,SUBNET_INFO);
            popupINFO("Dades del projecte: \n"+selectedFile.getAbsolutePath()+"\n Han sigut carregades amb exit","Projecte");
        } else {
            popupERROR("El projecte esta buit o \nhi ha algun camp que no es correcte","ERROR DE PROJECTE");
        }

    }

    /**
     * aquesta funció retorna el cidr i la IP de el projecte carregat de forma binaria i no binaria, ho retorna en format de carta
     */
    public void report_IP_binari(){
        System.out.println(PROJECT_DATA.getALL());
        String[] segmentedIP = PROJECT_DATA.getIP().split("\\.");
        String[] segmentedCIDR = PROJECT_DATA.getCIDR().split("/");
        StringBuilder binaryIP = new StringBuilder();
        StringBuilder binaryCIDR = new StringBuilder();
        for(String i : segmentedIP){
            int value = Integer.parseInt(i);
            String binaryOctet = String.format("%08d", Integer.parseInt(Integer.toBinaryString(value))); // Asegura 8 bits
            binaryIP.append(binaryOctet);
        }
        for (int i = 0; i < 32; i++) {
            if (i < Integer.valueOf(segmentedCIDR[1])) {
                binaryCIDR.append("1");
            } else {
                binaryCIDR.append("0");
            }
        }

        if (report_area != null) {
            report_area.setText("+----------------------Report Binari----------------------+\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t  |\n" +
                                        "|\tIP:{"+PROJECT_DATA.getIP()+"}\t\t\t\t\t\t\t  |\n" +
                                        "|\tCIDR: {"+PROJECT_DATA.getCIDR()+"}\t\t\t\t\t\t\t\t  |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t  |\n" +
                                        "|IP Binaria:    {"+binaryIP+"}    |\n" +
                                        "|CIDR Binari: {"+binaryCIDR+"}    |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t  |\n" +
                                        "+-----------------------------------------------------------+");
        }
    }

    /**
     * aquesta funcio fa un report amb informació sobre la mascara de xarxa
     */
    public void report_mascara(){
        String[] segmentedCIDR = PROJECT_DATA.getCIDR().split("/");
        StringBuilder binaryCIDR = new StringBuilder();
        StringBuilder mascaraCIDR = new StringBuilder();
        StringBuilder mask_part = new StringBuilder();
        StringBuilder host_part = new StringBuilder();
        //binari
        for (int i = 0; i < 32; i++) {
            if (i < Integer.valueOf(segmentedCIDR[1])) {
                binaryCIDR.append("1");
                mask_part.append("1");
            } else {
                binaryCIDR.append("0");
                host_part.append("0");
            }
        }
        //mascara (format --> 255.255.255.0)
        for (int i = 0; i < 4; i++) {
            String segment = binaryCIDR.substring(i * 8, (i + 1) * 8);
            mascaraCIDR.append(Integer.parseInt(segment,2));
            if (i < 3) {
                mascaraCIDR.append(".");
            }
        }
        if (report_area != null) {
            report_area.setText("+----------------------Report Màscara----------------------+\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "|\tCIDR: {" + PROJECT_DATA.getCIDR() + "}\t\t\t\t\t\t\t\t     |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "|CIDR Binari: {" + binaryCIDR + "}\t     |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "|Máscara Decimal: {" + mascaraCIDR + "}\t\t\t\t\t     |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "|Mascara: {"+mask_part+"}\t\t\t     |\n" +
                                        "|Host: {"+host_part+"}\t\t\t\t\t\t\t\t     |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "+-------------------------------------------------------------+");
        }

    }

    /**
     * aquesta funció retorna un report amb informació del rang d'ip, ip de xarxa/broadcasti hosts/subxarxes possibles
     */
    public void report_IP_rang(){
        int hosts_possibles=1;
        String[] segmentedIP = PROJECT_DATA.getIP().split("\\.");
        String[] segmentedCIDR = PROJECT_DATA.getCIDR().split("/");
        StringBuilder binaryCIDR = new StringBuilder();
        StringBuilder binaryIP = new StringBuilder();
        StringBuilder broadcast = new StringBuilder();
        //ip a binari
        for(String i : segmentedIP){
            int value = Integer.parseInt(i);
            String binaryOctet = String.format("%08d", Integer.parseInt(Integer.toBinaryString(value))); // Asegura 8 bits
            binaryIP.append(binaryOctet);
        }
        //mascara a binari
        for (int i = 0; i < 32; i++) {
            if (i < Integer.valueOf(segmentedCIDR[1])) {
                binaryCIDR.append("1");
            } else {
                binaryCIDR.append("0");
            }
        }
        //broadcast
        for (int i = 0; i < 32; i++) {
            if (binaryCIDR.charAt(i)=='0'){
                binaryIP.setCharAt(i,'1');
            }
        }
        for (int i = 0; i < 4; i++) {
            String segment = binaryIP.substring(i * 8, (i + 1) * 8);
            broadcast.append(Integer.parseInt(segment,2));
            if (i < 3) {
                broadcast.append(".");
            }
        }
        //hosts
        hosts_possibles = (int) Math.pow(2, 32-Integer.valueOf(segmentedCIDR[1])) - 2;

        if (report_area != null) {
            report_area.setText("+----------------------Report Rang IP----------------------+\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "|\tIP:{"+PROJECT_DATA.getIP()+"}\t\t\t\t\t\t\t     |\n" +
                                        "|\tCIDR: {" + PROJECT_DATA.getCIDR() + "}\t\t\t\t\t\t\t\t     |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "|Subxarxes possibles: {" + (hosts_possibles+2) + "}\t\t\t\t\t\t     |\n" +
                                        "|Hosts possibles: {" + hosts_possibles + "}\t\t\t\t\t\t\t     |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "|IP Xarxa: {"+PROJECT_DATA.getIP()+PROJECT_DATA.getCIDR()+"}\t\t\t\t\t\t     |\n" +
                                        "|IP Brooadcast: {"+broadcast+"}\t\t\t\t\t     |\n" +
                                        "|\t\t\t\t\t\t\t\t\t\t\t     |\n" +
                                        "+-------------------------------------------------------------+");
        }
    }

    /**
     * retorna un report que engloba els reports de rang, mascara i binari
     */
    public void report_total() {
        System.out.println("triggered");
        String[] segmentedIP = PROJECT_DATA.getIP().split("\\.");
        String[] segmentedCIDR = PROJECT_DATA.getCIDR().split("/");

        StringBuilder binaryIP = new StringBuilder();
        StringBuilder binaryCIDR = new StringBuilder();
        StringBuilder binaryMask = new StringBuilder();
        StringBuilder broadcast = new StringBuilder();
        StringBuilder maskDecimal = new StringBuilder();

        // Reporte IP Binari
        for (String i : segmentedIP) {
            int value = Integer.parseInt(i);
            String binaryOctet = String.format("%08d", Integer.parseInt(Integer.toBinaryString(value)));
            binaryIP.append(binaryOctet);
        }

        // Reporte CIDR Binari
        for (int i = 0; i < 32; i++) {
            if (i < Integer.valueOf(segmentedCIDR[1])) {
                binaryCIDR.append("1");
            } else {
                binaryCIDR.append("0");
            }
        }

        // Reporte Màscara de xarxa en format binari
        StringBuilder maskPart = new StringBuilder();
        StringBuilder hostPart = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            if (i < Integer.valueOf(segmentedCIDR[1])) {
                binaryMask.append("1");
                maskPart.append("1");
            } else {
                binaryMask.append("0");
                hostPart.append("0");
            }
        }

        // Màscara en format decimal (255.255.255.0)
        for (int i = 0; i < 4; i++) {
            String segment = binaryMask.substring(i * 8, (i + 1) * 8);
            maskDecimal.append(Integer.parseInt(segment, 2));
            if (i < 3) {
                maskDecimal.append(".");
            }
        }

        // Càlcul de la direcció de broadcast
        for (int i = 0; i < 32; i++) {
            if (binaryCIDR.charAt(i) == '0') {
                binaryIP.setCharAt(i, '1');
            }
        }
        for (int i = 0; i < 4; i++) {
            String segment = binaryIP.substring(i * 8, (i + 1) * 8);
            broadcast.append(Integer.parseInt(segment, 2));
            if (i < 3) {
                broadcast.append(".");
            }
        }

        // Càlcul dels hosts possibles
        int hosts_possibles = (int) Math.pow(2, 32 - Integer.valueOf(segmentedCIDR[1])) - 2;

        // Generació del report total
        if (report_area != null) {
            report_area.setText(
                    "+-------------------------Report Total---------------------------+\n" +
                            "|\t\t\t\t\t\t\t\t\t\t\t\t   |\n" +
                            "|\tIP:{"+PROJECT_DATA.getIP()+"}\t\t\t\t\t\t\t\t   |\n" +
                            "|\tCIDR: {" + PROJECT_DATA.getCIDR() + "}\t\t\t\t\t\t\t\t\t   |\n" +
                            "|\t\t\t\t\t\t\t\t\t\t\t\t   |\n" +
                            "|IP Binaria:    {"+binaryIP+"}\t\t   |\n" +
                            "|CIDR Binari:   {"+binaryCIDR+"}\t\t   |\n" +
                            "|Màscara Decimal: {" + maskDecimal + "}\t\t\t\t\t\t   |\n" +
                            "|\t\t\t\t\t\t\t\t\t\t\t\t   |\n" +
                            "|Màscara: {"+maskPart+"}\t\t\t\t\t   |\n" +
                            "|Host: {"+hostPart+"}\t\t\t\t\t\t\t\t\t   |\n" +
                            "|\t\t\t\t\t\t\t\t\t\t\t\t   |\n" +
                            "|Subxarxes possibles: {" + (hosts_possibles+2) + "}\t\t\t\t\t\t\t   |\n" +
                            "|Hosts possibles: {" + hosts_possibles + "}\t\t\t\t\t\t\t\t   |\n" +
                            "|\t\t\t\t\t\t\t\t\t\t\t\t   |\n" +
                            "|IP Xarxa: {"+PROJECT_DATA.getIP()+PROJECT_DATA.getCIDR()+"}\t\t\t\t\t\t\t   |\n" +
                            "|IP Brooadcast: {"+broadcast+"}\t\t\t\t\t\t   |\n" +
                            "|\t\t\t\t\t\t\t\t\t\t\t\t   |\n" +
                            "+-----------------------------------------------------------------+"
            );
        }
    }



    public class generalFunctions implements Initializable {
        //variables y objetos
        @FXML //botones
        private Button lan_info_home_btn, home_menu_lan_info, cip, calcularIP_tohome_btn;
        @FXML
        private TextField ruta_projecte, n_projecte, ip_xarxa, n_routers, n_switch, n_subxarxes, n_switchos; // Añadido n_switchos
        @FXML
        private ChoiceBox mascares;
        @FXML
        private TextArea names_users_subx, report_area, config_recommendations; // Añadido config_recommendations

        //datos de los proyectos existentes INICIO
        String projectName = "";
        String IP = "";
        String CIDR = "";
        String ROUTERS = "";
        String SWITCH = "";
        String SUBNET = "";
        String SUBNET_INFO = "";

        // Array para almacenar la configuración de cada switch
        private String[] switchConfigurations;

        //EXPRESIONES REGULARES (verificaciones de archivo/datos)
        String empty_textfield = "[^\\d\\w\\s]"; // verifica que no haya caracteres o números
        String is_number = "\\d+"; // verifica que solo haya números
        String is_IP = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$"; // verifica si es una IP válida
        String istxt = "[^\\W]\\w+\\.txt$";
        String corrtctTextArea = "[\\w]+:[\\s|\\t]*[\\d]+[^\\W]";
        String IP_check = "^IP_DE_XARXA=\\d{1,3}(\\.\\d{1,3}){3}$";
        String cidr_check = "^MASCARA_DE_XARXA=\\/(30|[12]?[1-9])$";
        String general_info_check = "(ROUTERS|SWITCH|SUBXARXA)=[\\d]";

        // para cambios de escena
        private Stage stage;
        private Scene scene;
        private Parent root;

        DirectoryChooser dc = new DirectoryChooser(); //abre a partir del buscador de archivos
        project_Data PROJECT_DATA;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            System.out.println("Initialize triggered");
            fillmask();
        }

        @FXML
        public void fillmask() {
            System.out.println("fillmask triggered");
            if (mascares == null) {
                System.out.println("Error: mascares no está inicializado o no está en el formulario adecuado");
                return;
            }
            for (int i = 1; i <= 30; i++) {
                mascares.getItems().add("/" + i);
            }
            System.out.println("Opciones añadidas al ChoiceBox");
        }

        //condicional para que el numero de switches sea en la cantidad necesaria
        @FXML
        public void configureSwitchos() {
            try {
                int numSwitchos = Integer.parseInt(n_switchos.getText());

                if (numSwitchos <= 0) {
                    popupERROR("El número de switchos debe ser mayor que cero.", "Error");
                    return;
                } else if (numSwitchos > 48) {
                    popupERROR("El número máximo permitido de switchos es 48.", "Error");
                    return;
                }

                // Inicializar el array para almacenar configuraciones
                switchConfigurations = new String[numSwitchos];

                StringBuilder recommendations = new StringBuilder("=== Pasos Básicos para Configurar un Switch ===\n\n");//comentrios de las instrucciones para poder configurar los switches
                recommendations.append("1. Conéctate al switch mediante consola o SSH.\n");
                recommendations.append("2. Entra en modo privilegiado usando el comando 'enable'.\n");
                recommendations.append("3. Configura el nombre del switch con 'hostname'.\n");
                recommendations.append("4. Crea y asigna VLANs según la necesidad.\n");
                recommendations.append("5. Configura enlaces troncales con 'switchport mode trunk'.\n");
                recommendations.append("6. Configura una dirección IP en la interfaz VLAN para la administración.\n");
                recommendations.append("7. Guarda los cambios con el comando 'write memory'.\n\n");

                recommendations.append("=== Configuración específica para ").append(numSwitchos).append(" switchos ===\n\n");

                for (int i = 1; i <= numSwitchos; i++) {
                    StringBuilder switchConfig = new StringBuilder("=== Configuración para Switch ").append(i).append(" ===\n");
                    //pequeña reseña por cada switch seleccionado
                    switch (i) {
                        case 1:
                            switchConfig.append("- Switch principal (root bridge): Configura RSTP, VLAN de administración y enlaces troncales.\n");
                            break;
                        case 2:
                            switchConfig.append("- Switch para tráfico clave: Implementa VLANs específicas y Port Security.\n");
                            break;
                        case 3:
                            switchConfig.append("- Switch para servidores: Habilita VLANs exclusivas y QoS.\n");
                            break;
                        case 4:
                            switchConfig.append("- Switch para puntos de acceso inalámbrico: Configura VLANs Wi-Fi y medidas de seguridad como 802.1X.\n");
                            break;
                        case 5:
                            switchConfig.append("- Switch para redundancia: Configura enlaces redundantes y backups automáticos.\n");
                            break;
                        default:
                            switchConfig.append("- Switch genérico: Configura VLANs y supervisa con SNMP.\n");
                            break;
                    }

                    switchConfigurations[i - 1] = switchConfig.toString(); // Guardar en el array
                    recommendations.append(switchConfig).append("\n");
                }

                config_recommendations.setText(recommendations.toString());
                popupINFO("Configuración recomendada generada con éxito.", "Información");

            } catch (NumberFormatException e) {
                popupERROR("Por favor, introduce un número válido de switchos.", "Error");
            }
        }

        public void popupINFO(String message, String header) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        }

        public void popupERROR(String message, String header) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}
