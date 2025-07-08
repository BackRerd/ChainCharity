// mockData.js
export const mockUsers = {
    unselected: {
        user_id: 1,
        username: 'user123',
        phone: '13800138000',
        is_admin: 0,
        user_status: 'active',
        user_type: '',
        is_enterprise: 0,
        profile: null
    },
    donor: {
        user_id: 2,
        username: 'donor_user',
        phone: '13800138001',
        is_admin: 0,
        user_status: 'active',
        user_type: 'donor',
        is_enterprise: 0,
        profile: {
            profile_id: 1,
            user_id: 2,
            user_type: 'donor',
            real_name: '张三',
            id_card_number: '110101199001011234',
            avatar: 'https://example.com/avatars/1.jpg',
            gender: 'male',
            birth_date: '1990-01-01',
            address: '北京市朝阳区',
            id_verified: 1,
            bank_verified: 0,
            enterprise_verified: 0
        }
    },
    recipient: {
        user_id: 3,
        username: 'recipient_user',
        phone: '13800138002',
        is_admin: 0,
        user_status: 'active',
        user_type: 'recipient',
        is_enterprise: 0,
        profile: {
            profile_id: 2,
            user_id: 3,
            user_type: 'recipient',
            real_name: '李四',
            id_card_number: '110101198502022345',
            avatar: 'https://example.com/avatars/2.jpg',
            gender: 'female',
            birth_date: '1985-02-02',
            address: '上海市浦东新区',
            bank_account: '6225880123456789',
            bank_name: '招商银行',
            needs_description_file: 'https://example.com/docs/needs.pdf',
            family_members: 3,
            monthly_income: 2500.00,
            poverty_level: 'medium',
            id_verified: 1,
            bank_verified: 1,
            enterprise_verified: 0
        }
    },
    enterprise: {
        user_id: 4,
        username: 'company_user',
        phone: '13800138003',
        is_admin: 0,
        user_status: 'active',
        user_type: 'donor',
        is_enterprise: 1,
        profile: {
            profile_id: 3,
            user_id: 4,
            user_type: 'enterprise',
            real_name: '爱心科技有限公司',
            id_card_number: '91310115789012345X',
            avatar: 'https://example.com/logos/1.png',
            legal_representative: '王五',
            business_license: '123456789012345',
            enterprise_type: 'private',
            industry: '互联网',
            contact_person: '赵六',
            contact_phone: '13800138004',
            enterprise_role: ['can_donate', 'can_receive'],
            id_verified: 1,
            bank_verified: 0,
            enterprise_verified: 1
        }
    }
}