package www.kfstudio.com.oympartner;

public class Booking {
    private String booker_email;
    private String booker_name;
    private String booker_phone;
    private String booking_for;
    private String booking_time;
    private Boolean booking_done;

    public Boolean getBooking_done() {
        return booking_done;
    }

    public void setBooking_done(Boolean booking_done) {
        this.booking_done = booking_done;
    }

    public Booking() {

    }
    public Booking(String booker_email, String booker_name, String booker_phone, String booking_for, String booking_time) {
        this.booker_email = booker_email;
        this.booker_name = booker_name;
        this.booker_phone = booker_phone;
        this.booking_for = booking_for;
        this.booking_time = booking_time;
    }

    public String getBooker_email() {
        return booker_email;
    }

    public void setBooker_email(String booker_email) {
        this.booker_email = booker_email;
    }

    public String getBooker_name() {
        return booker_name;
    }

    public void setBooker_name(String booker_name) {
        this.booker_name = booker_name;
    }

    public String getBooker_phone() {
        return booker_phone;
    }

    public void setBooker_phone(String booker_phone) {
        this.booker_phone = booker_phone;
    }

    public String getBooking_for() {
        return booking_for;
    }

    public void setBooking_for(String booking_for) {
        this.booking_for = booking_for;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }
}
