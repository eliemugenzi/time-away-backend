package dev.elieweb.timeaway.enums;

public enum LeaveType {
    PERSONAL_TIME_OFF(20, "Personal Time Off (PTO)"),
    SICK_LEAVE(15, "Sick Leave"),
    MATERNITY_LEAVE(84, "Maternity Leave"), // 12 weeks as per Rwandan law
    PATERNITY_LEAVE(4, "Paternity Leave"),
    COMPASSIONATE_LEAVE(5, "Compassionate Leave"),
    STUDY_LEAVE(0, "Study Leave"), // Duration to be determined by agreement
    UNPAID_LEAVE(0, "Unpaid Leave"); // Duration to be determined by agreement

    private final int defaultDays;
    private final String description;

    LeaveType(int defaultDays, String description) {
        this.defaultDays = defaultDays;
        this.description = description;
    }

    public int getDefaultDays() {
        return defaultDays;
    }

    public String getDescription() {
        return description;
    }
}
