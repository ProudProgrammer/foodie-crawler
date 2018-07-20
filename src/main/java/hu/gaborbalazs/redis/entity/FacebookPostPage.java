package hu.gaborbalazs.redis.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookPostPage {

	@JsonProperty("data")
	private List<FacebookPost> posts;
	private FacebookPaging paging;

	public List<FacebookPost> getPosts() {
		if (posts == null) {
			posts = new ArrayList<>();
		}
		return posts;
	}

	public void setPosts(List<FacebookPost> posts) {
		this.posts = posts;
	}

	public FacebookPaging getPaging() {
		return paging;
	}

	public void setPaging(FacebookPaging paging) {
		this.paging = paging;
	}

	@Override
	public String toString() {
		return "FacebookPostPage [posts=" + posts + ", paging=" + paging + "]";
	}

}
