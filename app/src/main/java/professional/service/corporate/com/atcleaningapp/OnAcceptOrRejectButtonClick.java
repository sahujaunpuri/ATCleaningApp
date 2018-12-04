package professional.service.corporate.com.atcleaningapp;

interface OnAcceptOrRejectButtonClick {
    public void onAcceptPressed(int bid_id, int project_id, int bidder_id);

    public void onRejectPressed(int bid_id, int project_id, int bidder_id);
}
