package com.example.practice.service;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import com.example.practice.R;
import com.example.practice.bean.ContactInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/2.
 */
public class ContactInfoParser {

    /**
     * 获取系统全部联系人的API方法
     *
     * @param context
     * @return
     */
    public static List<ContactInfo> findAll(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);

        while (cursor.moveToNext()) {
            String id = cursor.getColumnName(0);
            if (id != null) {
                // 2. 根据联系人的id，查询data表，把这个id的数据取出来
                // 系统api 查询data表的时候 不是真正的查询data表 而是查询的data表的视图
                ContactInfo info = new ContactInfo();
                Cursor dataCursor = resolver.query(datauri, new String[]{
                                "data1", "mimetype"}, "raw_contact_id=?",
                        new String[]{id}, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        System.out.println("姓名=" + data1);
                        info.setName(data1);
                    } else if ("vnd.android.cursor.item/email_v2"
                            .equals(mimetype)) {
                        System.out.println("邮箱=" + data1);
                        info.setEmail(data1);
                    } else if ("vnd.android.cursor.item/phone_v2"
                            .equals(mimetype)) {
                        System.out.println("电话=" + data1);
                        info.setPhone(data1);
                    } else if ("vnd.android.cursor.item/im".equals(mimetype)) {
                        System.out.println("QQ=" + data1);
                        info.setQq(data1);
                    }
                }
                infos.add(info);
                System.out.println("------");
                dataCursor.close();
            }
        }
        return infos;
    }

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;


    /**得到手机通讯录联系人信息**/
    public static List<ContactInfo> getPhoneContacts(Context context) {
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        ContentResolver resolver = context.getContentResolver();

// 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);


        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                ContactInfo info = new ContactInfo();

                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if(photoid > 0 ) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }else {
                    contactPhoto = BitmapFactory.decodeResource(context.getResources(), R.drawable.a);
                }

                info.setId(contactid + "");
                info.setName(contactName);
                info.setPhone(phoneNumber);
                infos.add(info);
            }

            phoneCursor.close();

        }

        return infos;
    }

}
