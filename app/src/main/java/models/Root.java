package models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Root
{
    @SerializedName("html_attributions")
    private List<String> htmlAttributes;

    @SerializedName("results")
    private List<Results> results;

    private String status;


    public List<String> getHtmlAttributes() {
        return htmlAttributes;
    }

    public void setHtmlAttributes(List<String> htmlAttributes) {
        this.htmlAttributes = htmlAttributes;
    }

    public void setResults(List<Results> results){
        this.results = results;
    }
    public List<Results> getResults(){
        return this.results;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
}
