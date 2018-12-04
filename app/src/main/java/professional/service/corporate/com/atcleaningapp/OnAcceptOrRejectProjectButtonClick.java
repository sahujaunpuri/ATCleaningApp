package professional.service.corporate.com.atcleaningapp;

interface OnAcceptOrRejectProjectButtonClick {
    public void accepting_a_project(final int bid_id, final int project_id, final int bidder_id);
    public void rejecting_a_project(final int bid_id, final int project_id, final int bidder_id);

    void accepting_a_project_by_Contractor(int project_id, int assignee_id, int bid_id);

    void rejecting_a_project_by_Contractor(int project_id, int assignee_id, int bid_id);
}
