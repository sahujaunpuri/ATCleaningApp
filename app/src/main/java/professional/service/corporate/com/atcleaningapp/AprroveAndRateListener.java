package professional.service.corporate.com.atcleaningapp;

interface AprroveAndRateListener {
    public void approveCompletionRequest(int project_id, int assignee_id);
    public void rateContractor(int project_id, int assignee_id);
}
