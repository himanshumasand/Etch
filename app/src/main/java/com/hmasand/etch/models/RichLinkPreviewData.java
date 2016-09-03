package com.hmasand.etch.models;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

/**
 * Created by hmasand on 9/2/16.
 */
public class RichLinkPreviewData {

    public CreateRichLinkPreviewListener listener;

    public String url;
    public String title;
    public String description;
    public String imageUrl;

    public RichLinkPreviewData() {}

    public RichLinkPreviewData(String url, String title, String description, String imageUrl) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public RichLinkPreviewData(CreateRichLinkPreviewListener listener, String url) {
        this.listener = listener;
        this.url = url;
        new TextCrawler().makePreview(linkPreviewCallback, url);
    }

    private LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {
        @Override
        public void onPre() {

        }

        @Override
        public void onPos(SourceContent sourceContent, boolean b) {

            title = sourceContent.getTitle();
            description = sourceContent.getDescription();
            if (sourceContent.getImages() != null && sourceContent.getImages().size() > 0) {
                imageUrl = sourceContent.getImages().get(0);
            }

            listener.onCreateRichLinkPreviewSuccess(new RichLinkPreviewData(url, title, description, imageUrl));

        }
    };

    public interface CreateRichLinkPreviewListener {
        void onCreateRichLinkPreviewSuccess(RichLinkPreviewData data);
    }

}
