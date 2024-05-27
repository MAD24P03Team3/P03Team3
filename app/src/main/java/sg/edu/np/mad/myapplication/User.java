package sg.edu.np.mad.myapplication;

abstract class User {
    String name;
    String studentId;
    String password;

    public User(String name, String studentId, String password){
        this.name = name;
        this.studentId = studentId;
        this.password = password;
    }
    public String getStudentId() {
        return studentId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
