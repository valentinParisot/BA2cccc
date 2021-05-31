package ch.epfl.tchu.gui;

public final class StringsFr {
    private StringsFr() {
    }

    // Nom des cartes
    public static final String BLACK_CARD = "noire";
    public static final String BLUE_CARD = "bleue";
    public static final String GREEN_CARD = "verte";
    public static final String ORANGE_CARD = "orange";
    public static final String RED_CARD = "rouge";
    public static final String VIOLET_CARD = "violette";
    public static final String WHITE_CARD = "blanche";
    public static final String YELLOW_CARD = "jaune";
    public static final String LOCOMOTIVE_CARD = "locomotive";

    // Étiquettes des boutons
    public static final String TICKETS = "Billets";
    public static final String CARDS = "Cartes";
    public static final String CHOOSE = "Choisir";

    // Titre des fenêtres
    public static final String TICKETS_CHOICE = "Choix de billets";
    public static final String CARDS_CHOICE = "Choix de cartes";

    // Invites
    public static final String CHOOSE_TICKETS =
            "Choisissez au moins %s billet%s parmi ceux-ci :";
    public static final String CHOOSE_CARDS =
            "Choisissez les cartes à utiliser pour vous emparer de cette route :";
    public static final String CHOOSE_ADDITIONAL_CARDS =
            "Choisissez les cartes supplémentaires à utiliser pour vous" +
                    " emparer de ce tunnel (ou aucune pour annuler et passer votre tour) :";

    // Informations concernant le déroulement de la partie
    public static final String WILL_PLAY_FIRST =
            "%s jouera en premier.\n\n";
    public static final String KEPT_N_TICKETS =
            "%s a gardé %s billet%s.\n";
    public static final String CAN_PLAY =
            "\nC'est à %s de jouer.\n";
    public static final String DREW_TICKETS =
            "%s a tiré %s billet%s...\n";
    public static final String DREW_BLIND_CARD =
            "%s a tiré une carte de la pioche.\n";
    public static final String DREW_VISIBLE_CARD =
            "%s a tiré une carte %s visible.\n";
    public static final String CLAIMED_ROUTE =
            "%s a pris possession de la route %s au moyen de %s.\n";
    public static final String ATTEMPTS_TUNNEL_CLAIM =
            "%s tente de s'emparer du tunnel %s au moyen de %s !\n";
    public static final String ADDITIONAL_CARDS_ARE =
            "Les cartes supplémentaires sont %s. ";
    public static final String NO_ADDITIONAL_COST =
            "Elles n'impliquent aucun coût additionnel.\n";
    public static final String SOME_ADDITIONAL_COST =
            "Elles impliquent un coût additionnel de %s carte%s.\n";
    public static final String DID_NOT_CLAIM_ROUTE =
            "%s n'a pas pu (ou voulu) s'emparer de la route %s.\n";
    public static final String LAST_TURN_BEGINS =
            "\n%s n'a plus que %s wagon%s, le dernier tour commence !\n";
    public static final String GETS_BONUS =
            "\n%s reçoit un bonus de 10 points pour le plus long trajet (%s).\n";
    public static final String WINS =
            "\n%s remporte la victoire avec %s point%s, contre %s point%s !\n";
    public static final String DRAW =
            "\n%s sont ex æqo avec %s points !\n";

    // Statistiques des joueurs
    public static final String PLAYER_STATS =
            " %s :\n– %s billets,\n– %s cartes,\n– %s wagons,\n– %s points.";

    // Séparateurs textuels
    public static final String AND_SEPARATOR = " et ";
    public static final String EN_DASH_SEPARATOR = " – ";

    /**
     * Retourne une chaîne marquant le pluriel, ou la chaîne vide.
     *
     * @param value la valeur déterminant la chaîne retournée
     * @return la chaîne vide si la valeur vaut ±1, la chaîne "s" sinon
     */
    public static String plural(int value) {

            return Math.abs(value) <= 1 ? "" : "s";

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String BLACK_CARD_CHINOIS = "黑色的";
    public static final String BLUE_CARD_CHINOIS = "藍色的";
    public static final String GREEN_CARD_CHINOIS = "綠色";
    public static final String ORANGE_CARD_CHINOIS = "橙子";
    public static final String RED_CARD_CHINOIS = "紅色的";
    public static final String VIOLET_CARD_CHINOIS = "紫色";
    public static final String WHITE_CARD_CHINOIS = "白色的";
    public static final String YELLOW_CARD_CHINOIS = "黃色的";
    public static final String LOCOMOTIVE_CARD_CHINOIS = "機車";

    // Étiquettes des boutons
    public static final String TICKETS_CHINOIS = "門票";
    public static final String CARDS_CHINOIS = "牌";
    public static final String CHOOSE_CHINOIS = "選擇";

    // Titre des fenêtres
    public static final String TICKETS_CHOICE_CHINOIS = "門票選擇";
    public static final String CARDS_CHOICE_CHINOIS = "卡片的選擇";

    // Invites
    public static final String CHOOSE_TICKETS_CHINOIS =
            "從以下選項中至少選擇 %s 票 %s :";
    public static final String CHOOSE_CARDS_CHINOIS =
            "選擇使用哪些地圖來抓取這條路線：";
    public static final String CHOOSE_ADDITIONAL_CARDS_CHINOIS =
            "選擇要使用的附加卡" + " 佔領這條隧道（或不取消並跳過你的回合）：";

    // Informations concernant le déroulement de la partie
    public static final String WILL_PLAY_FIRST_CHINOIS =
            "％s 首先播放。\n\n";
    public static final String KEPT_N_TICKETS_CHINOIS =
            "%s 保留 %s 票%s。\n";
    public static final String CAN_PLAY_CHINOIS =
            "\n它是在 %s 玩。\n";
    public static final String DREW_TICKETS_CHINOIS =
            "%s 被解僱 %s 票%s。。。\n";
    public static final String DREW_BLIND_CARD_CHINOIS =
            "%s 從抽牌堆中抽了一張牌。\n";
    public static final String DREW_VISIBLE_CARD_CHINOIS =
            "%s 畫了一張可見的 %s 卡片。\n";
    public static final String CLAIMED_ROUTE_CHINOIS =
            "%s 通過以下方式佔據了 %s 路線 %s。\n";
    public static final String ATTEMPTS_TUNNEL_CLAIM_CHINOIS =
            "%s 試圖接管隧道 %s 通過 %s !\n";
    public static final String ADDITIONAL_CARDS_ARE_CHINOIS =
            "附加卡是 %s。 ";
    public static final String NO_ADDITIONAL_COST_CHINOIS =
            "他們不涉及任何額外費用。\n";
    public static final String SOME_ADDITIONAL_COST_CHINOIS =
            "它們涉及額外的成本 %s 菜單%s。\n";
    public static final String DID_NOT_CLAIM_ROUTE_CHINOIS =
            "%s 不能（或不想）走這條路 %s。\n";
    public static final String LAST_TURN_BEGINS_CHINOIS =
            "\n%s 只有 %s 車%s, 最後一輪開始 !\n";
    public static final String GETS_BONUS_CHINOIS =
            "\n%s 獲得最長旅程的 10 積分獎勵 (%s)。\n";
    public static final String WINS_CHINOIS =
            "\n%s 贏得 %s 觀點%s, 反對 %s 觀點%s !\n";
    public static final String DRAW_CHINOIS =
            "\n%s 與 %s 積分 !\n";

    // Statistiques des joueurs
    public static final String PLAYER_STATS_CHINOIS =
            " %s :\n– %s 門票,\n– %s 牌,\n– %s 貨車,\n– %s 積分。";

    // Séparateurs textuels
    public static final String AND_SEPARATOR_CHINOIS = " 和 ";
    public static final String EN_DASH_SEPARATOR_CHINOIS = " —— ";
}
