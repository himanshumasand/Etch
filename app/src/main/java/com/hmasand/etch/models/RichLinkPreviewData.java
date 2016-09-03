package com.hmasand.etch.models;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

/**
 * Created by hmasand on 9/2/16.
 */
public class RichLinkPreviewData {

    public CreateRichLinkPreviewListener listener;
    public String title;
    public String description;
    public String imageUrl;

    public RichLinkPreviewData(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public RichLinkPreviewData(CreateRichLinkPreviewListener listener, String url) {
        this.listener = listener;
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

            listener.onCreateRichLinkPreviewSuccess(new RichLinkPreviewData(title, description, imageUrl));

        }
    };

    public interface CreateRichLinkPreviewListener {
        void onCreateRichLinkPreviewSuccess(RichLinkPreviewData data);
    }

}
