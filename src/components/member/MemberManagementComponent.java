package components.member;

import domain.member.Member;
import repositories.MemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberManagementComponent {
    private final MemberRepository memberRepository;

    public MemberManagementComponent(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member addMember(String fullName) {
        return memberRepository.save(new Member(fullName));
    }

    public Optional<Member> findMemberById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return memberRepository.findById(Math.toIntExact(id));
    }

    public List<Member> listMembers() {
        return memberRepository.findAll();
    }
}
