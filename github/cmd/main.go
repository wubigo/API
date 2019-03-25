// Copyright 2017 The go-github AUTHORS. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

// The simple command demonstrates a simple functionality which
// prompts the user for a GitHub username and lists all the public
// organization memberships of the specified username.
package main

import (
"context"
"fmt"

"github.com/google/go-github/v24/github"
)

// Fetch all the public organizations' membership of a user.
//
func FetchOrganizations(username string) ([]*github.Organization, error) {
	client := github.NewClient(nil)
	orgs, _, err := client.Organizations.List(context.Background(), username, nil)
	return orgs, err
}

// Fetch all repos.
//
func FetchRepos(username string) ([]*github.Repository, error) {
	client := github.NewClient(nil)
	repos, _, err := client.Repositories.List(context.Background(), username, nil)
	return repos, err
}

func main() {
	var username string
	// fmt.Print("Enter GitHub username: ")
	// fmt.Scanf("%s", &username)
	username = "wubigo"

	organizations, err := FetchOrganizations(username)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return
	}

	num := len(organizations)

	if num == 0 {
		fmt.Printf("No organization exist for %s\n", username)

	} else {

		for i, organization := range organizations {
			fmt.Printf("%v. %v\n", i+1, organization.GetLogin())
		}
	}

	repos, err := FetchRepos(username)
	num = len(repos)
	if num == 0 {
		fmt.Printf("No repos exist for %s\n", username)

	} else {
		for i, repo := range repos {
			fmt.Printf("%v. %v\n", i+1, repo.GetName())
		}

	}
}

