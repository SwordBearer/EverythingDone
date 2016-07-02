package com.ywwynm.everythingdone.model;

import android.content.Context;
import android.database.Cursor;

import com.ywwynm.everythingdone.R;
import com.ywwynm.everythingdone.utils.DateTimeUtil;
import com.ywwynm.everythingdone.utils.LocaleUtil;

import java.util.Calendar;

/**
 * Created by ywwynm on 2015/8/4.
 * Model layer. Related to table "reminders".
 */
public class Reminder {

    /**
     * if you want to change the determined time for goal, you should change it in
     * {@link #getType(long, long)},
     * {@link com.ywwynm.everythingdone.database.ReminderDAO#resetGoal(Reminder)},
     *
     */

    public static final int UNDERWAY = 0;
    public static final int REMINDED = 2;
    public static final int EXPIRED  = 3;

    public static final int  GOAL_DAYS = 28;
    public static final long GOAL_MILLIS = GOAL_DAYS * 24 * 60 * 60 * 1000L;

    private long id;
    private long notifyTime;
    private int state;
    private long notifyMillis;
    private long createTime;
    private long updateTime;

    public Reminder(long id, long notifyTime) {
        this.id = id;
        this.notifyTime = notifyTime;
        initNotifyMinutes();
        long curTime = System.currentTimeMillis();
        this.createTime = curTime;
        this.updateTime = curTime;
    }

    public Reminder(long id, long notifyTime, int state, long notifyMillis, long createTime, long updateTime) {
        this.id = id;
        this.notifyTime = notifyTime;
        this.state = state;
        this.notifyMillis = notifyMillis;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Reminder(Cursor c) {
        this(c.getLong(0), c.getLong(1), c.getInt(2),
                c.getLong(3), c.getLong(4), c.getLong(5));
    }

    public void initNotifyMinutes() {
        notifyMillis = notifyTime - System.currentTimeMillis();
        notifyMillis += 10; // make it a whole number, not 1999999 but 2000000
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getNotifyMillis() {
        return notifyMillis;
    }

    public void setNotifyMillis(long notifyMillis) {
        this.notifyMillis = notifyMillis;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCelebrationText(Context context) {
        String part1 = context.getString(R.string.celebration_goal_part_1);
        String day = context.getString(R.string.days);
        String part2 = context.getString(R.string.celebration_goal_part_2);
        String part3 = context.getString(R.string.celebration_goal_part_3);
        int gap = DateTimeUtil.calculateTimeGap(updateTime, System.currentTimeMillis(), Calendar.DATE);
        gap++;

        boolean isChinese = LocaleUtil.isChinese(context);
        if (gap > 1 && !isChinese) {
            day += "s";
        }
        return part1 + " " + gap + " " + day + (isChinese ? "" : " ") + part2 + "\n" + part3;
    }

    public static boolean noUpdate(Reminder reminder, long notifyTime, int state) {
        return reminder.notifyTime == notifyTime && reminder.state == state;
    }

    public static @Thing.Type int getType(long notifyTime, long createTime) {
        if (DateTimeUtil.calculateTimeGap(createTime, notifyTime, Calendar.DATE) > GOAL_DAYS) {
            return Thing.GOAL;
        } else return Thing.REMINDER;
    }

    public static String getStateDescription(int thingState, int reminderState, Context context) {
        String result;
        if (reminderState == REMINDED) {
            result = context.getString(R.string.reminder_reminded);
        } else if (reminderState == EXPIRED) {
            result = context.getString(R.string.reminder_expired);
        } else {
            if (thingState == Thing.UNDERWAY) {
                result = "";
            } else if (thingState == Thing.FINISHED) {
                result = context.getString(R.string.reminder_needless);
            } else {
                result = context.getString(R.string.reminder_unavailable);
            }
        }
        return result;
    }
}