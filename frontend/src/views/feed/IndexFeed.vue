<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12" sm="4" offset-sm="4" class="pb-0">
        <search-bar 
                style="color:red"
                outlined inputValue="text" placeholder="검색어를 입력해주세요." label="검색" @search="searchFeeds" />
      </v-col>
    </v-row>

    <v-row class="pa-0" style="justify-content:center;">
      <v-col cols="12" md="4" v-for="feed in feeds" v-bind:key="feed.feedNo" class="ma-1 py-0">
        <FeedItem2 :feed="feed"/>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" sm="6" offset-sm="3">
        <infinite-loading
          slot="append"
          ref="infiniteLoading"
          @infinite="infiniteHandler"
          force-use-infinite-wrapper=".el-table__body-wrapper"
        >
          <div slot="no-results">결과가 없습니다</div>
        </infinite-loading>
      </v-col>
    </v-row>

  </v-container>
</template>

<script>
import { mapState } from "vuex";
import "../../components/css/feed/feed-item.scss";
import "../../components/css/feed/newsfeed.scss";
import FeedItem2 from "../../components/feed/FeedItem2.vue";
import FeedApi from "../../api/FeedApi";
import InfiniteLoading from "vue-infinite-loading";
import SearchBar from "../../components/feed/SearchBar";

export default {
  props: ["keyword"],
  data: () => {
    return {
      page: 0,
      feeds: [],
      text: "",
      error: {
        email: "",
        text: "",
      },
      isSearch : false,
      search : "",
      isWorldcup: false,
      worldcup : ""
    }
  },
  components: { 
    SearchBar, 
    FeedItem2, 
    InfiniteLoading 
  },
  created() {
    if(this.$route.params.worldcup) {
      this.isWorldcup = true;
      this.worldcup = this.$route.params.worldcup;
    }
  },
  methods : {
    infiniteHandler($state) {
      if(!this.isSearch && !this.isWorldcup) {
        let data = {
          token: localStorage.getItem("token"),
          page: this.page,
        };
        FeedApi.loadFeeds(
          data,
          (response) => {
            if (response.data.content.length) {
              this.page += 1;
              
              this.feeds = this.feeds.concat(response.data.content);
              
              $state.loaded();
            } else {
              $state.complete();
            }
          },
          (error) => {
            alert(error);
          }
        )
      } else if(this.isSearch){
        let data = {
          token: localStorage.getItem("token"),
          search: this.search,
          page: this.page,
        };
        FeedApi.searchFeeds(
          data,
          (response) => {
            if (response.data.length) {
              this.page += 1;
              this.feeds = this.feeds.concat(response.data);
              $state.loaded();
            } else {
              $state.complete();
            }
          },
          (error) => {
            alert(error);
          }
        )
      } else {
        let data = {
          token : localStorage.getItem("token"),
          worldcup : this.worldcup,
          page : this.page
        };
        FeedApi.worldcupFeeds(
          data,
          response => {
            if (response.data.length) {
              this.page += 1;
              this.feeds = this.feeds.concat(response.data);
              $state.loaded();
            } else {
              $state.complete();
            }
          },
          error => {
            alert(error);
          }
        )
      }
    },
    searchFeeds(search) {
      this.isSearch = true;
      this.search = search;
      this.isWorldcup = false;
      this.worldcup = "";
      this.feeds = [];
      this.page = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
    },
  },
};
</script>
<style scoped>
</style>
