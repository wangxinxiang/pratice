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
     * ��ȡϵͳȫ����ϵ�˵�API����
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
                // 2. ������ϵ�˵�id����ѯdata�������id������ȡ����
                // ϵͳapi ��ѯdata���ʱ�� ���������Ĳ�ѯdata�� ���ǲ�ѯ��data�����ͼ
                ContactInfo info = new ContactInfo();
                Cursor dataCursor = resolver.query(datauri, new String[]{
                                "data1", "mimetype"}, "raw_contact_id=?",
                        new String[]{id}, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        System.out.println("����=" + data1);
                        info.setName(data1);
                    } else if ("vnd.android.cursor.item/email_v2"
                            .equals(mimetype)) {
                        System.out.println("����=" + data1);
                        info.setEmail(data1);
                    } else if ("vnd.android.cursor.item/phone_v2"
                            .equals(mimetype)) {
                        System.out.println("�绰=" + data1);
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

    /**��ȡ��Phon���ֶ�**/
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    /**��ϵ����ʾ����**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**�绰����**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**ͷ��ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**��ϵ�˵�ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;


    /**�õ��ֻ�ͨѶ¼��ϵ����Ϣ**/
    public static List<ContactInfo> getPhoneContacts(Context context) {
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        ContentResolver resolver = context.getContentResolver();

// ��ȡ�ֻ���ϵ��
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);


        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                ContactInfo info = new ContactInfo();

                //�õ��ֻ�����
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //�õ���ϵ������
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //�õ���ϵ��ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //�õ���ϵ��ͷ��ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //�õ���ϵ��ͷ��Bitamp
                Bitmap contactPhoto = null;

                //photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�
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
