package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.test.lsm.R;

import butterknife.BindView;

/**
 * 功能：隐私政策
 *
 * @author yu
 * @version 1.0
 * @date 2018/9/11
 */
public class PrivacyPolicyActivity extends LsmBaseActivity {

    @BindView(R.id.tv_show)
    TextView tvShow;

    public static final String content =
            "<p style=\"text-align:center\">\n" +
            "    <strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 19pxfont-family:Microsoft JhengHei\">隱私權保護政策</span></strong><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 19px\">&nbsp;</span></strong>\n" +
            "</p>\n" +
            "<p style=\"text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13px\"><span style=\"font-family:Microsoft JhengHei\">歡迎您使用</span>BEATINFO應用程式(以下簡稱本APP)，為了幫助您瞭解奇翼醫電股份有限公司(以下簡稱本公司)如何透過APP蒐集、使用及保護您所提供之個人資料，請您開始使用前，務必詳讀下列條款內容。若您點擊「同意」，即表示您已閱讀、了解並同意遵守下列條款：</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">一、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">隱私權保護政策適用範圍</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">隱私權保護政策內容，包括本公司如何處理您在使用本</span>APP時蒐集到之個人資料。隱私權保護政策不適用於本APP以外之相關連結，亦不適用於非本公司所委託或參與管理之人員。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">二、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">個人資料蒐集目的</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <a href=\"http://undefined\"></a><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">本公司為提供您最佳之個人化服務，並進行客戶管理、業務行銷、統計與分析、資訊與資料庫管理之目的，於您使用本</span>APP時將主動蒐集您所產生之相關個人資料。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">三、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">個人資料蒐集</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">1.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">本公司蒐集並儲存任何您提供或允許存取之訊息，包括但不限於姓名、肖像、身高、體重、地址、手機號碼、電子郵件地址、性別、出生日期、聯絡人名單等資料，必要時亦包括信用卡及支付相關訊息。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">2.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">本公司除會保留您所提供之上述資料外，其他因您使用本</span>APP所產生之數據，包括IP位址、登錄時間、位置資料、本APP之Cookie資料、手機軟體和硬體屬性、瀏覽器以及使用者之點選記錄，伺服器也將自動接收並記錄。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">3.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">本公司不會故意蒐集</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">未滿</span>13歲或依當地法律另行規定之未成年</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">之個人資料。若您</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">為未成年人，您應該</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">與您監護人一起閱讀本隱私權保護政策，</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">並取得您監護人之同意後始得</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">使用本</span>APP</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">。</span><span style=\"font-family: &quot;Microsoft JhengHei&quot;; font-size: 13px;\">&nbsp;</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">四、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">個人資料處理及利用</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">本公司搜集到之個人資料，將做如下處理：</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">1.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">您提供之基本個人資料，將作為本</span>APP服務期間身分判定之依據。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">2.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">提升及個性化您的使用體驗，使您能精確使用本</span>APP。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">3.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">因使用本</span>APP所產生之資料，將作為改進本APP服務或開發新服務用。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">4.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">本公司將透過行動推播或寄送電子郵件之方式，發送給您管理訊息及其他關於本公司之推廣訊息。若您不希望收到管理訊息或推廣訊息，可隨</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">時</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">取消此通知服務。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">5.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">本公司可能將「綜合性非個人資料」提供予第三方使用。「綜合性非個人資料」是指記錄有關使用者使用並被分</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">類</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">蒐集之資料，這些資料在經過分</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">類</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">後</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">將</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">不再</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">識</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">別</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">出</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">使用者</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">。上述資料之處理與利用，可能由本公司之關係企業或其他合作夥伴進行，或於中華民國境外發生。除非有當地法律除外情況，本公司不會將這些資訊用於</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">蒐集目的以外</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">用途。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">6.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">本公司不會在未經您事先同意之情況下向第三者透露其個人資料，但因國家重大利益或為配合執法機關調查根據法律而需要透露者除外。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">7.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">除綜合性非個人資料外，本公司可以為本政策所述之目的處理及使用您的訊息，至該等目的不復存在、或相關法律或契約規定之保存年限、或您同意之其他較長期限屆期為止。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">五、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">個人資料公開共享</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">1.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">當您參與或使用特定服務時，您同意公開分享一些基本訊息，可能包括您的使用者名稱、所在城市或頭像圖片等。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">2.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">為了參與特定功能，您可能需要調整您的隱私設定並且分享特定訊息。您也可以選擇將您的活動於其他平台上共享，例如</span>WhatsApp或Wechat。此時請閱讀該等平台之隱私政策，因為您在該等平台上公開的活動將不再適用本公司之隱私政策。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">3.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">您公開共享之訊息可能會被本公司用於</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">行銷推廣</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">目的。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">六、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">個人資料之自主權</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">1.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">您可查詢、複製、申請變動或閱覽屬於您本人之個人資料，若非您本人或於本公司尚未查證為您本人前，不得申請查詢、複製、請求閱覽或申請變動不屬於該帳號之個人資料及帳號服務。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">2.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">您可要求停止蒐集、處理、利用或刪除屬於您本人之個人資料；惟一經停止或刪除，您將無法繼續使用本</span>APP之服務。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">3.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">於妨害國家重大利益、妨害公務機關執行法定職務、妨害蒐集機關或第三人重大利益時，本公司有權拒絕您主動提出變更、刪除或異動個人資料之請求。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">七、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">個人資料之安全</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">1.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">為保護您的隱私，本公司提供必要之技術及安全防護措施保護個人資料，包括加密及驗證工具。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">2.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">您的個人資料、註冊帳號及密碼等，請妥善保管，避免外洩。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">3.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">您同意於使用本</span>APP服務時，所提供及使用之資料皆為正確且合法，並無侵害第三人權利、違反第三方協議或涉及任何違法行為。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">4.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">縱使本公司採用多項保護措施，但無法就您提供之訊息提供任何安全性或保密性之保證</span><span style=\"font-family: &quot;Microsoft JhengHei&quot;; font-size: 13px;\">&nbsp;</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">八、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">免責條款</span></strong><strong></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">1.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">本公司不保證本</span>APP與您使用之行動裝置完全相容，亦不保證本APP無任何錯誤或不中斷。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">2.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">本</span>APP提供之相關資訊僅供您參考及運用，如該資料有任何錯誤或與實際不符之處，本公司不負相關責任。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">3.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">本</span>APP可能提供之第三方網站連結，其目的僅為方便您使用，然此不代表該等第三方網站之內容為本公司所認可與負責。如您連結至第三方網站或廣告，所有因此而生之風險，應由您自行承擔，本公司不會為您與任何廣告商或第三方網站經營者之間之任何交易承擔任何責任。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:51px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">4.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">如因使用本</span>APP而致第三人損害，除本公司故意或重大過失外，本公司並不負擔相關賠償責任。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:27px\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-weight:bold;font-size:13px\">九、&nbsp;</span><strong><span style=\"font-family: &#39;Microsoft JhengHei&#39;;font-size: 13pxfont-family:Microsoft JhengHei\">條款修訂與效力</span></strong>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">1.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">本公司保留未來修改本政策之權利，並遵循中華民國個人資料保護相關法令。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">2.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">本政策新增或修改時將公告於</span>APP，當您按下同意鍵繼續使用時，即視為您已同意本政策最新內容。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">3.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\"><span style=\"font-family:Microsoft JhengHei\">如您不同意本政策或修改變更之內容，請立即停止使用本</span>APP。</span>\n" +
            "</p>\n" +
            "<p style=\"margin-left:56px;text-align:justify;text-justify:inter-ideograph\">\n" +
            "    <span style=\"font-family:&#39;Microsoft JhengHei&#39;;font-size:13px\">4.&nbsp;</span><span style=\";font-family:&#39;Microsoft JhengHei&#39;;font-size:13pxfont-family:Microsoft JhengHei\">如您對本公司之隱私權或個人資料保護政策有任何問題，請聯絡客服：</span><a href=\"http://undefined\"><span style=\"text-decoration:underline;\"><span style=\"font-family: &#39;Microsoft JhengHei&#39;;color: rgb(5, 99, 193);font-size: 13px\">service</span></span><span style=\"text-decoration:underline;\"><span style=\"font-family: &#39;Microsoft JhengHei&#39;;color: rgb(5, 99, 193);font-size: 13px\">@</span></span><span style=\"text-decoration:underline;\"><span style=\"font-family: &#39;Microsoft JhengHei&#39;;color: rgb(5, 99, 193);font-size: 13px\">singularwings</span></span><span style=\"text-decoration:underline;\"><span style=\"font-family: &#39;Microsoft JhengHei&#39;;color: rgb(5, 99, 193);font-size: 13px\">.com</span></span></a>\n" +
            "</p>\n" +
            "<p>\n" +
            "    <br/>\n" +
            "</p>";

    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy_policy;
    }

    @Override
    protected void initView() {
        tvShow.setText(Html.fromHtml(content));
    }

    @Override
    protected void initListener() {

    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, PrivacyPolicyActivity.class);
        context.startActivity(intent);
    }

}
