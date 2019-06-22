package pl.umk.mat.mtracewicz.gui.contentPanels;

import org.mindrot.jbcrypt.BCrypt;
import pl.umk.mat.mtracewicz.entity.Users;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingPane extends ContentPanel {
    private JPanel loginPane;
    private JPanel registerPane;
    private JPanel loggedPane;
    private Agenda agenda;
    @Override
    public void resetPanel(){
        if(agenda.isLogged()) {
            this.showPanel("logged");
        }else{
            this.showPanel("login");
        }
    }

    private void changePassword(){
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter new password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(okCxl != JOptionPane.CANCEL_OPTION) {
            if (passwordIsValid(pf.getPassword())) {
                    Users user = new Users();
                    user.setEmail(agenda.getUsername());
                    agenda.connector.changeUserPassword(user,pf.getPassword());
            } else {
                JOptionPane.showMessageDialog(window, "<html>*Password must contain at least:<br>" +
                        "-one digit<br>" +
                        "-one lower case letter<br>" +
                        "-one upper case letter<br>" +
                        "-one special character<br>" +
                        "*It can't have white spaces.<br>" +
                        "*It must be at least 8 characters long</html>", "Inane error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean checkLoginCredentials(String email,char[] password){
        Users user = new Users(email,String.valueOf(password));
        return agenda.connector.checkUserCredentials(user);
    }
    private boolean emailIsValid(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean passwordIsValid(char[] password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!_@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(password));
        return matcher.matches();
    }
    private void loginUser(String login,char [] password){
        if(login.isEmpty() || String.valueOf(password).isEmpty()){
            JOptionPane.showMessageDialog(window, "Provide username and password", "Inane error", JOptionPane.ERROR_MESSAGE);
        }else if(!checkLoginCredentials(login,password)){
            JOptionPane.showMessageDialog(window, "Invalid username or password", "Inane error", JOptionPane.ERROR_MESSAGE);
        }else {
            agenda.setLogged(true);
            agenda.setUsername(login);
            JPanel newLabel = (JPanel) (loggedPane.getComponent(1));
            JLabel label = (JLabel) newLabel.getComponent(0);
            label.setText("Hello " + agenda.getUsername() + "!");
            this.showPanel("logged");
        }
    }
    private void registerUser(String email,char[] pass1,char[] pass2){
        String errorMessage = null;
        if(email.isEmpty() || String.valueOf(pass1).isEmpty() || String.valueOf(pass2).isEmpty()){
            errorMessage = "Fill in all fields!";
        }else if(Arrays.equals(pass1,pass2)) {
            if (passwordIsValid(pass1)) {
                if (emailIsValid(email)) {
                    Users newUser = new Users(email, BCrypt.hashpw(String.valueOf(pass1),BCrypt.gensalt()));
                    String returned = agenda.connector.addUser(newUser);
                    if(returned.equals("succes")) {
                        JPanel panel2 = (JPanel)window.getComponent(1);
                        JPanel panel = (JPanel)panel2.getComponent(1);
                        ((JTextField)panel.getComponent(1)).setText(null);
                        ((JPasswordField)panel.getComponent(3)).setText(null);
                        ((JPasswordField)panel.getComponent(5)).setText(null);
                        loginUser(email, pass1);
                    }else {
                        errorMessage = returned;
                    }
                } else {
                    errorMessage = "Enter valid email addres";
                }
            }else {
                errorMessage = "<html>*Password must contain at least:<br>" +
                        "-one digit<br>" +
                        "-one lower case letter<br>" +
                        "-one upper case letter<br>" +
                        "-one special character<br>" +
                        "*It can't have white spaces.<br>" +
                        "*It must be at least 8 characters long</html>";
            }
        }else{
            errorMessage = "Passwords don't match";
        }
        if(errorMessage != null){
            JOptionPane.showMessageDialog(window, errorMessage, "Inane error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void logoutUser(){
        agenda.setLogged(false);
        agenda.setUsername(null);
        this.showPanel("login");
        this.clearPanel(footer);
    }
    private void deleteUser(){
        Users u = new Users();
        u.setEmail(agenda.getUsername());
        agenda.connector.deleteUser(u);
        this.logoutUser();
    }
    private JPanel createLoginPane(){
        JPanel panel = new JPanel();
        JPanel menu = new JPanel();
        JTextField emailText = new JTextField();
        JPasswordField passwordText = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e ->{
            this.loginUser(emailText.getText(),passwordText.getPassword());
            emailText.setText(null);
            passwordText.setText(null);
        });
        registerButton.addActionListener(e -> {
            this.showPanel("register");
            emailText.setText(null);
            passwordText.setText(null);
        });

        menu.setLayout(new GridLayout(3,2));
        menu.add(new JLabel("Email:"));
        menu.add(emailText);
        menu.add(new JLabel("Password:"));
        menu.add(passwordText);
        passwordText.addActionListener(e -> loginButton.doClick());
        menu.add(loginButton);
        menu.add(registerButton);
        JPanel whitespace = new JPanel();
        this.setUpSize(whitespace,400,300, AgendaConstants.ALL);
        this.setUpSize(menu,400,100,AgendaConstants.ALL);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(whitespace);
        panel.add(menu);

        return panel;
    }
    private JPanel createRegisterPane(){
        JPanel panel = new JPanel();
        JPanel menu = new JPanel();
        JTextField emailText = new JTextField();
        JPasswordField passwordText = new JPasswordField();
        JPasswordField repeatPasswordText = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("<-Back");
        JPanel buttonPane = new JPanel(new GridLayout(1,2));

        buttonPane.add(backButton);
        buttonPane.add(registerButton);

        passwordText.setToolTipText("<html>*Password must contain at least:<br>" +
                "-one digit<br>" +
                "-one lower case letter<br>" +
                "-one upper case letter<br>" +
                "-one special character<br>" +
                "*It can't have white spaces.<br>" +
                "*It must be at least 8 characters long</html>");
        repeatPasswordText.setToolTipText("Must match password");

        emailText.setName("emailText");
        passwordText.setName("passwordText");
        repeatPasswordText.setName("repeatPasswordText");

        menu.setLayout(new GridLayout(4,2));
        menu.add(new JLabel("e-mail:"));
        menu.add(emailText);
        menu.add(new JLabel("Password:"));
        menu.add(passwordText);
        menu.add(new JLabel("Repeat Password:"));
        menu.add(repeatPasswordText);
        menu.add(new JPanel());
        menu.add(buttonPane);

        JPanel whitespace = new JPanel();
        this.setUpSize(whitespace,400,300,AgendaConstants.ALL);

        this.setUpSize(menu,400,100,AgendaConstants.ALL);

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(whitespace);
        panel.add(menu);
        registerButton.addActionListener(e->{
            registerUser(emailText.getText(),passwordText.getPassword(),repeatPasswordText.getPassword());
        });
        backButton.addActionListener(e->{
            emailText.setText(null);
            passwordText.setText(null);
            repeatPasswordText.setText(null);
            this.showPanel("login");
        });
        return panel;
    }
    private JPanel createLoggedPane(){
        JPanel panel = new JPanel();
        JPanel buttons = new JPanel(new GridLayout(1,3));
        JButton password = new JButton("Change password");
        JButton logoutButton = new JButton("Logout");
        JButton deleteAccount = new JButton("Delete account");

        JPanel whitespace = new JPanel();
        this.setUpSize(whitespace,300,200,AgendaConstants.ALL);

        password.addActionListener(e ->  changePassword());
        logoutButton.addActionListener(e -> logoutUser());
        deleteAccount.addActionListener(e -> deleteUser());

        buttons.add(deleteAccount);
        buttons.add(password);
        buttons.add(logoutButton);


        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(whitespace);
        JLabel loginLabel = new JLabel("Login",SwingConstants.CENTER);
        loginLabel.setFont(new Font(loginLabel.getFont().getName(),Font.BOLD,loginLabel.getFont().getSize()));
        JPanel labels = new JPanel();
        labels.setLayout(new GridLayout(2,1));

        labels.add(loginLabel);
        labels.add(buttons);

        this.setUpSize(labels,600,300,AgendaConstants.ALL);
        panel.add(labels);

        return panel;
    }
    public SettingPane(Agenda agenda) {
        super();
        this.agenda = agenda;
        loginPane = createLoginPane();
        registerPane = createRegisterPane();
        loggedPane = createLoggedPane();

        window.add("login",loginPane);
        window.add("register",registerPane);
        window.add("logged",loggedPane);
    }
}