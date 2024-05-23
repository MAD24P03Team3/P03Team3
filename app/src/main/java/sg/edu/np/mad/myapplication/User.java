package sg.edu.np.mad.myapplication;

abstract class User {
    String studentId;
    String password;

    public User(String studentId, String password){
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
}
