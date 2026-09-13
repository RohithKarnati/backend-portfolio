package com.dev.rohith.portfolio.developer;

import com.dev.rohith.portfolio.developer.command.CommandRegistry;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.command.impl.AboutCommand;
import com.dev.rohith.portfolio.developer.command.impl.CatCommand;
import com.dev.rohith.portfolio.developer.command.impl.ChangeDirectoryCommand;
import com.dev.rohith.portfolio.developer.command.impl.ContactCommand;
import com.dev.rohith.portfolio.developer.command.impl.ExperienceCommand;
import com.dev.rohith.portfolio.developer.command.impl.GithubCommand;
import com.dev.rohith.portfolio.developer.command.impl.HelpCommand;
import com.dev.rohith.portfolio.developer.command.impl.ListCommand;
import com.dev.rohith.portfolio.developer.command.impl.ProjectsCommand;
import com.dev.rohith.portfolio.developer.command.impl.PwdCommand;
import com.dev.rohith.portfolio.developer.command.impl.SkillsCommand;
import com.dev.rohith.portfolio.developer.command.impl.WhoAmICommand;
import com.dev.rohith.portfolio.developer.dto.TerminalRequest;
import com.dev.rohith.portfolio.developer.dto.TerminalResponse;
import com.dev.rohith.portfolio.developer.filesystem.VirtualFileSystem;
import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import com.dev.rohith.portfolio.developer.service.TerminalService;
import com.dev.rohith.portfolio.dto.GithubUser;
import com.dev.rohith.portfolio.service.GithubService;
import com.dev.rohith.portfolio.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Wires the real command/filesystem/service objects by hand (no Spring context needed) with a
 * mocked {@link GithubService}, so these stay fast and never touch the real GitHub API.
 */
class TerminalServiceTest {

    private static final String HOME = VirtualFileSystem.HOME;

    private GithubService githubService;
    private CommandRegistry registry;
    private TerminalService terminalService;

    @BeforeEach
    void setUp() {
        githubService = mock(GithubService.class);
        ProfileService profileService = new ProfileService(githubService);
        TerminalContentService content = new TerminalContentService(profileService);
        VirtualFileSystem fileSystem = new VirtualFileSystem(content);

        List<TerminalCommand> commands = List.of(
                new HelpCommand(),
                new WhoAmICommand(profileService),
                new PwdCommand(),
                new ListCommand(fileSystem),
                new ChangeDirectoryCommand(fileSystem),
                new CatCommand(fileSystem),
                new AboutCommand(content),
                new ExperienceCommand(content),
                new SkillsCommand(content),
                new ProjectsCommand(content),
                new GithubCommand(content),
                new ContactCommand(content)
        );

        registry = new CommandRegistry(commands);
        terminalService = new TerminalService(registry);
    }

    @Test
    void registryFindsRegisteredCommand() {
        assertThat(registry.find("ls")).isPresent();
        assertThat(registry.find("LS")).isPresent(); // case-insensitive
    }

    @Test
    void registryDoesNotFindUnknownCommand() {
        assertThat(registry.find("bogus")).isEmpty();
    }

    @Test
    void clearAndHistoryAreNotBackendCommands() {
        // Phase 1: these are handled entirely on the frontend.
        assertThat(registry.find("clear")).isEmpty();
        assertThat(registry.find("history")).isEmpty();
    }

    @Test
    void unknownCommandReturnsError() {
        TerminalResponse response = terminalService.run(new TerminalRequest("bogus", HOME));

        assertThat(response.type()).isEqualTo("ERROR");
        assertThat(response.output()).isEqualTo("bogus: command not found");
        assertThat(response.currentPath()).isEqualTo(HOME);
    }

    @Test
    void pwdReturnsCurrentPath() {
        TerminalResponse response = terminalService.run(new TerminalRequest("pwd", HOME + "/projects"));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.output()).isEqualTo(HOME + "/projects");
    }

    @Test
    void lsAtRootListsExpectedEntries() {
        TerminalResponse response = terminalService.run(new TerminalRequest("ls", HOME));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.output())
                .contains("README.md")
                .contains("about.txt")
                .contains("contact.txt")
                .contains("experience/")
                .contains("github/")
                .contains("projects/")
                .contains("resume/")
                .contains("skills/");
    }

    @Test
    void lsInNestedDirectoryListsItsContents() {
        TerminalResponse response = terminalService.run(new TerminalRequest("ls", HOME + "/skills"));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.output()).contains("backend.txt");
        assertThat(response.output().lines().count()).isEqualTo(6); // one file per tech category
    }

    @Test
    void cdIntoValidDirectorySucceeds() {
        TerminalResponse response = terminalService.run(new TerminalRequest("cd experience", HOME));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.currentPath()).isEqualTo(HOME + "/experience");
    }

    @Test
    void cdIntoInvalidDirectoryFailsWithoutChangingPath() {
        TerminalResponse response = terminalService.run(new TerminalRequest("cd nonexistent", HOME));

        assertThat(response.type()).isEqualTo("ERROR");
        assertThat(response.output()).isEqualTo("cd: nonexistent: No such directory");
        assertThat(response.currentPath()).isEqualTo(HOME);
    }

    @Test
    void cdMissingOperandFails() {
        TerminalResponse response = terminalService.run(new TerminalRequest("cd", HOME));

        assertThat(response.type()).isEqualTo("ERROR");
        assertThat(response.output()).isEqualTo("cd: missing operand");
    }

    @Test
    void cdDotDotGoesUpOneLevel() {
        TerminalResponse response = terminalService.run(new TerminalRequest("cd ..", HOME + "/experience"));

        assertThat(response.currentPath()).isEqualTo(HOME);
    }

    @Test
    void cdTildeReturnsHomeFromAnywhere() {
        TerminalResponse response = terminalService.run(new TerminalRequest("cd ~", HOME + "/projects"));

        assertThat(response.currentPath()).isEqualTo(HOME);
    }

    @Test
    void catValidFileReturnsContent() {
        TerminalResponse response = terminalService.run(new TerminalRequest("cat about.txt", HOME));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.output()).contains("Rohith Kumar");
    }

    @Test
    void catFileInSubdirectoryUsingRelativePath() {
        TerminalResponse response = terminalService.run(
                new TerminalRequest("cat experience/senior-software-engineer.txt", HOME));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.output()).contains("Morgan Stanley").contains("[PRODUCTION]");
    }

    @Test
    void catInvalidFileReturnsError() {
        TerminalResponse response = terminalService.run(new TerminalRequest("cat nope.txt", HOME));

        assertThat(response.type()).isEqualTo("ERROR");
        assertThat(response.output()).isEqualTo("cat: nope.txt: No such file or directory");
    }

    @Test
    void pathTraversalCannotEscapeTheVirtualFilesystem() {
        TerminalResponse cdResponse = terminalService.run(new TerminalRequest("cd ../../../../etc", HOME));
        assertThat(cdResponse.type()).isEqualTo("ERROR");
        assertThat(cdResponse.currentPath()).isEqualTo(HOME);

        TerminalResponse catResponse = terminalService.run(
                new TerminalRequest("cat ../../../../../etc/passwd", HOME));
        assertThat(catResponse.type()).isEqualTo("ERROR");
    }

    @Test
    void githubCommandDelegatesToExistingGithubService() {
        GithubUser user = new GithubUser("RohithKarnati", null, 11, 3, 2, "https://github.com/RohithKarnati", "2022-08-20T00:00:00Z");
        when(githubService.fetchGithubProfile(ProfileService.GITHUB_USERNAME)).thenReturn(user);

        TerminalResponse response = terminalService.run(new TerminalRequest("github", HOME));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.output()).contains("RohithKarnati").contains("11");
    }

    @Test
    void githubCommandFailsCleanlyWhenServiceUnavailable() {
        when(githubService.fetchGithubProfile(ProfileService.GITHUB_USERNAME)).thenReturn(null);

        TerminalResponse response = terminalService.run(new TerminalRequest("github", HOME));

        assertThat(response.type()).isEqualTo("ERROR");
        assertThat(response.output()).isEqualTo("github: unable to retrieve GitHub information");
    }

    @Test
    void commandParsingSplitsNameAndArguments() {
        TerminalResponse response = terminalService.run(new TerminalRequest("  cd   experience  ", HOME));

        assertThat(response.type()).isEqualTo("TEXT");
        assertThat(response.currentPath()).isEqualTo(HOME + "/experience");
    }
}
