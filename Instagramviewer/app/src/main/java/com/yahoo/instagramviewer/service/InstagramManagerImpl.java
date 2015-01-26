package com.yahoo.instagramviewer.service;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yahoo.instagramviewer.CommentEntries;
import com.yahoo.instagramviewer.PhotoEntries;
import com.yahoo.instagramviewer.datamodel.CommentData;
import com.yahoo.instagramviewer.datamodel.Data;
import com.yahoo.instagramviewer.datamodel.InstagramResponse;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandaja on 1/23/15.
 */
public class InstagramManagerImpl implements InstagramManager {

    public static final String FETCH_POPULAR_PHOTOS_URI = "https://api.instagram.com/v1/media/popular?client_id=595b40247cb84c4fb83e7f62b801d8ff&start=0";
    ;
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String args[]) {
        String testdata = "{\"meta\":{\"code\":200},\"data\":[{\"attribution\":null,\"tags\":[\"tfm\",\"tfmgirls\"],\"location\":null,\"comments\":{\"count\":606,\"data\":[{\"created_time\":\"1422069922\",\"text\":\"@emmadunbar26 @clairewolford @saramangiameli\",\"from\":{\"username\":\"laurenmange\",\"profile_picture\":\"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xfp1/t51.2885-19/10787737_671708939616704_826305303_a.jpg\",\"id\":\"50445263\",\"full_name\":\"Lauren Mangiameli\"},\"id\":\"904710537651571347\"},{\"created_time\":\"1422070762\",\"text\":\"Omg @cmckiernan3\",\"from\":{\"username\":\"paigevictor\",\"profile_picture\":\"https://igcdn-photos-h-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10848299_1606128019632839_622161419_a.jpg\",\"id\":\"20861503\",\"full_name\":\"paigevictor\"},\"id\":\"904717584141011110\"},{\"created_time\":\"1422070874\",\"text\":\"@arianna_lopez #flamingo?\",\"from\":{\"username\":\"m_kellyyy\",\"profile_picture\":\"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10899210_1534791740136440_534737281_a.jpg\",\"id\":\"211316632\",\"full_name\":\"Madeline Kelly\"},\"id\":\"904718529243531507\"},{\"created_time\":\"1422070999\",\"text\":\"@declancurnin_4 nice breasts!\",\"from\":{\"username\":\"richbagnato\",\"profile_picture\":\"https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10547215_1439467896326798_804871362_a.jpg\",\"id\":\"975986055\",\"full_name\":\"The 💣™\"},\"id\":\"904719574011103566\"},{\"created_time\":\"1422071182\",\"text\":\"@m_kellyyy omfg\",\"from\":{\"username\":\"arianna_lopez\",\"profile_picture\":\"https://instagramimages-a.akamaihd.net/profiles/profile_20605546_75sq_1395700717.jpg\",\"id\":\"20605546\",\"full_name\":\"Arianna Lopez\"},\"id\":\"904721109956839863\"},{\"created_time\":\"1422071184\",\"text\":\"😴💜💜 .\",\"from\":{\"username\":\"a7sasi_gher\",\"profile_picture\":\"https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10838720_305995362943769_2014634243_a.jpg\",\"id\":\"1380676178\",\"full_name\":\"_+ بوتْشِ 🌸 .\"},\"id\":\"904721127270926776\"},{\"created_time\":\"1422071228\",\"text\":\"Nice\",\"from\":{\"username\":\"mahdivoz61\",\"profile_picture\":\"https://instagramimages-a.akamaihd.net/profiles/anonymousUser.jpg\",\"id\":\"1503175798\",\"full_name\":\"mahdivoz61\"},\"id\":\"904721498961758683\"},{\"created_time\":\"1422071445\",\"text\":\"@usastreet\",\"from\":{\"username\":\"djtaz86\",\"profile_picture\":\"https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xap1/t51.2885-19/10507833_744422455599617_276911067_a.jpg\",\"id\":\"435329123\",\"full_name\":\"DJ TAZ\"},\"id\":\"904723317284817486\"}]},\"filter\":\"Normal\",\"created_time\":\"1422046922\",\"link\":\"http://instagram.com/p/yNffadRkkj/\",\"likes\":{\"count\":18987,\"data\":[{\"username\":\"main_train\",\"profile_picture\":\"https://instagramimages-a.akamaihd.net/profiles/profile_257773155_75sq_1393279991.jpg\",\"id\":\"257773155\",\"full_name\":\"Sean Main\"},{\"username\":\"yayamarsa\",\"profile_picture\":\"https://igcdn-photos-f-a.akamaihd.net/hphotos-ak-xap1/t51.2885-19/10723912_717428805017029_1546595678_a.jpg\",\"id\":\"226729650\",\"full_name\":\"Jayarti Rohmadani Marsa\"},{\"username\":\"_willhayward\",\"profile_picture\":\"https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xpa1/t51.2885-19/10787825_853761957979329_891714420_a.jpg\",\"id\":\"239358755\",\"full_name\":\"will hayward\"},{\"username\":\"alex_mckinney_\",\"profile_picture\":\"https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10584746_302231293291270_1782306351_a.jpg\",\"id\":\"23564709\",\"full_name\":\"Alex Mckinney⚾\"}]},\"images\":{\"low_resolution\":{\"url\":\"http://scontent-b.cdninstagram.com/hphotos-xaf1/t51.2885-15/s306x306/e15/10919319_1591693187729193_863959123_n.jpg\",\"width\":306,\"height\":306},\"thumbnail\":{\"url\":\"http://scontent-b.cdninstagram.com/hphotos-xaf1/t51.2885-15/s150x150/e15/10919319_1591693187729193_863959123_n.jpg\",\"width\":150,\"height\":150},\"standard_resolution\":{\"url\":\"http://scontent-b.cdninstagram.com/hphotos-xaf1/t51.2885-15/e15/10919319_1591693187729193_863959123_n.jpg\",\"width\":640,\"height\":640}},\"users_in_photo\":[{\"position\":{\"y\":0.7703125,\"x\":0.4046875},\"user\":{\"username\":\"idreamofgabby_\",\"profile_picture\":\"https://igcdn-photos-h-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10932166_1552697575001247_1886449980_a.jpg\",\"id\":\"282956098\",\"full_name\":\"Gabriella Romero\"}}],\"caption\":{\"created_time\":\"1422046922\",\"text\":\"@idreamofgabby_ from the University of Maryland. #TFM #TFMgirls\",\"from\":{\"username\":\"tfmgirls\",\"profile_picture\":\"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xap1/t51.2885-19/1389267_338122966349528_1298823109_a.jpg\",\"id\":\"1405454497\",\"full_name\":\"TFM Girls\"},\"id\":\"904517598870653502\"},\"type\":\"image\",\"id\":\"904517598325393699_1405454497\",\"user\":{\"username\":\"tfmgirls\",\"website\":\"\",\"profile_picture\":\"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xap1/t51.2885-19/1389267_338122966349528_1298823109_a.jpg\",\"full_name\":\"TFM Girls\",\"bio\":\"\",\"id\":\"1405454497\"}},{\"attribution\":null,\"tags\":[],\"location\":null,\"comments\":{\"count\":203,\"data\":[{\"created_time\":\"1422066325\",\"text\":\"Yesssssss @underscorelaur\",\"from\":{\"username\":\"angellecortez\",\"profile_picture\":\"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10802640_1572764529603832_1551800949_a.jpg\",\"id\":\"201908164\",\"full_name\":\"Angel Cortez\"},\"id\":\"904680366342421806\"},{\"created_time\":\"1422067080\",\"text\":\"Where in Hawaii\",\"from\":{\"username\":\"_jazlyn.49_\",\"profile_picture\":\"https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10914404_783336011702545_2103369061_a.jpg\",\"id\":\"1422884235\",\"full_name\":\"_jazlyn.49_\"},\"id\":\"904686704573300580\"},{\"created_time\":\"1422067203\",\"text\":\"@photobombowitz\",\"from\":{\"username\":\"matthewmaser\",\"profile_picture\":\"https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xpf1/t51.2885-19/10808426_833611200014310_1779054321_a.jpg\",\"id\":\"362958341\",\"full_name\":\"Matt Maser\"},\"id\":\"904687734912466878\"},{\"created_time\":\"1422069320\",\"text\":\"@katiemachon\",\"from\":{\"username\":\"nikimaragos\",\"profile_picture\":\"https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10853027_1599358076954617_422978003_a.jpg\",\"id\":\"14039247\",\"full_name\":\"Niki\"},\"id\":\"904705494090530234\"},{\"created_time\":\"1422069544\",\"text\":\"Tahiti is in hawaii :-) @nikimaragos\",\"from\":{\"username\":\"katiemachon\",\"profile_picture\":\"https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10665568_798517173547614_1895785035_a.jpg\",\"id\":\"21843444\",\"full_name\":\"katie machon\"},\"id\":\"904707368290106992\"},{\"created_time\":\"1422069949\",\"text\":\"Swwwett!\",\"from\":{\"username\":\"paganmario75\",\"profile_picture\":\"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10903697_745545785522816_1911865245_a.jpg\",\"id\":\"1636585875\",\"full_name\":\"Mario J. Pagan\"},\"id\":\"904710763193319284\"},{\"created_time\":\"1422070109\",\"text\":\"@chaseelk @teddymason\",\"from\":{\"username\":\"tonygiggatoni\",\"profile_picture\":\"https://instagramimages-a.akamaihd.net/profiles/profile_23908100_75sq_1366130343.jpg\",\"id\":\"23908100\",\"full_name\":\"TonyGiggatoni\"},\"id\":\"904712111183904740\"},{\"created_time\":\"1422070954\",\"text\":\"That's amazing!!\",\"from\":{\"username\":\"uzielracing\",\"profile_picture\":\"https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xpa1/t51.2885-19/10787954_1541507459427022_1873061788_a.jpg\",\"id\":\"1547217015\",\"full_name\":\"Piloto Uziel Medina\"},\"id\":\"904719199633161847\"}]},\"filter\":\"Normal\",\"created_time\":\"1422042489\",\"link\":\"http://instagram.com/p/yNXCUKnD0L/\",\"likes\":{\"count\":39958,\"data\":[{\"username\":\"takumi.k_1016\",\"profile_picture\":\"https://igcdn-photos-d-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10899508_326497747545987_359760312_a.jpg\",\"id\":\"645447433\",\"full_name\":\"Takumi Katoda\"},{\"username\":\"kmvang12\",\"profile_picture\":\"https://instagramimages-a.akamaihd.net/profiles/profile_428600355_75sq_1371877756.jpg\",\"id\":\"428600355\",\"full_name\":\"Kong Vang\"},{\"username\":\"jeffries_eustaquio_98\",\"profile_picture\":\"https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10914165_765411570209169_758466241_a.jpg\",\"id\":\"1652416876\",\"full_name\":\"Jeffries Eustaquio\"},{\"username\":\"jake_cookie23\",\"profile_picture\":\"https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10838807_886712558027756_93833715_a.jpg\",\"id\":\"346207726\",\"full_name\":\"jacob cooke\"}]},\"images\":{\"low_resolution\":{\"url\":\"http://scontent-a.cdninstagram.com/hphotos-xfa1/t51.2885-15/s306x306/e15/10950590_781631665256262_1771116805_n.jpg\",\"width\":306,\"height\":306},\"thumbnail\":{\"url\":\"http://scontent-a.cdninstagram.com/hphotos-xfa1/t51.2885-15/s150x150/e15/10950590_781631665256262_1771116805_n.jpg\",\"width\":150,\"height\":150},\"standard_resolution\":{\"url\":\"http://scontent-a.cdninstagram.com/hphotos-xfa1/t51.2885-15/e15/10950590_781631665256262_1771116805_n.jpg\",\"width\":640,\"height\":640}},\"users_in_photo\":[{\"position\":{\"y\":0.944,\"x\":0.9186667},\"user\":{\"username\":\"greguuz\",\"profile_picture\":\"https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10864764_834481456574176_1620187831_a.jpg\",\"id\":\"254629245\",\"full_name\":\"greguuz\"}}],\"caption\":{\"created_time\":\"1422042489\",\"text\":\"Hawaii with @greguuz 📷\",\"from\":{\"username\":\"alexisreneg\",\"profile_picture\":\"https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10831994_1518870238366780_2009565673_a.jpg\",\"id\":\"28656806\",\"full_name\":\"Alexis Ren\"},\"id\":\"904480414945262758\"},\"type\":\"image\",\"id\":\"904480414332894475_28656806\",\"user\":{\"username\":\"alexisreneg\",\"website\":\"\",\"profile_picture\":\"https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/10831994_1518870238366780_2009565673_a.jpg\",\"full_name\":\"Alexis Ren\",\"bio\":\"\",\"id\":\"28656806\"}}]}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            InstagramResponse response = mapper.readValue(testdata, InstagramResponse.class);
            System.out.println(response.getData().size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ArrayList<PhotoEntries> fetchPopularPhotos(final ArrayAdapter<PhotoEntries> adapter) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("q", "android");
        params.put("rsz", "100");
        client.get(FETCH_POPULAR_PHOTOS_URI, params, new
                        AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                try {
                                    InstagramResponse instagramResponse = mapper.readValue(response, InstagramResponse.class);
                                    List<Data> data = instagramResponse.getData();
                                    System.out.println(data.size());
                                    for (Data d : data) {
                                        PhotoEntries e = new PhotoEntries();
                                        if (d.getCaption() != null)
                                            e.setCaption(d.getCaption().getText());
                                        e.setDate(d.getCreated_time());

                                        if (d.getLikes() != null)
                                            e.setLikeCount(d.getLikes().getCount());
                                        e.setUrl(d.getImages().getStandard_resolution().getUrl());
                                        e.setUsername(d.getUser().getUsername());
                                        e.setUserProfilePic(d.getUser().getProfile_picture());
                                        if (d.getComments() != null) {

                                            e.setCommentCount(d.getComments().getCount().toString());
                                            if(d.getComments().getData().size()!=0) {
                                                CommentData c = d.getComments().getData().get(0);
                                                if (c != null) {
                                                    e.setComment1(c.getText());
                                                    e.setComment1User(c.getFrom().getUsername());
                                                }
                                                c = d.getComments().getData().get(1);
                                                if (c != null) {
                                                    e.setComment2(c.getText());
                                                    e.setComment2User(c.getFrom().getUsername());
                                                }
                                            }
                                        }

                                        ArrayList<CommentEntries> comments = new ArrayList<CommentEntries>();

                                        for(CommentData c : d.getComments().getData()){
                                            CommentEntries comment = new CommentEntries();
                                            comment.setCommentUser(c.getFrom().getUsername());
                                            comment.setComment(c.getText());
                                            comment.setCommentUserPicUrl(c.getFrom().getProfile_picture());
                                            comments.add(comment);
                                        }
                                        e.setComments(comments);
                                        adapter.add(e);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable e, String s) {
                                Log.d("ERROR", e.toString());
                            }
                        }
        );

        return null;
    }
}
